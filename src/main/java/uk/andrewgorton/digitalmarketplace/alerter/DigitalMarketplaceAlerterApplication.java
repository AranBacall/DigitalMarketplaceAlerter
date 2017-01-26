package uk.andrewgorton.digitalmarketplace.alerter;

import io.dropwizard.Application;
import io.dropwizard.assets.AssetsBundle;
import io.dropwizard.db.DataSourceFactory;
import io.dropwizard.jdbi.DBIFactory;
import io.dropwizard.migrations.MigrationsBundle;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;
import io.dropwizard.views.ViewBundle;
import io.dropwizard.views.ViewRenderer;
import org.apache.commons.validator.routines.EmailValidator;
import org.eclipse.jetty.server.session.SessionHandler;
import org.glassfish.hk2.utilities.binding.AbstractBinder;
import org.glassfish.jersey.process.internal.RequestScoped;
import org.skife.jdbi.v2.DBI;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import uk.andrewgorton.digitalmarketplace.alerter.dao.*;
import uk.andrewgorton.digitalmarketplace.alerter.dao.factory.UserDAOFactory;
import uk.andrewgorton.digitalmarketplace.alerter.dao.report.PieChartDAO;
import uk.andrewgorton.digitalmarketplace.alerter.email.*;
import uk.andrewgorton.digitalmarketplace.alerter.filters.AdminRequiredFeature;
import uk.andrewgorton.digitalmarketplace.alerter.filters.LoginRequiredFeature;
import uk.andrewgorton.digitalmarketplace.alerter.mappers.ForbiddenExceptionMapper;
import uk.andrewgorton.digitalmarketplace.alerter.mappers.IllegalArgumentExceptionMapper;
import uk.andrewgorton.digitalmarketplace.alerter.mappers.UnauthorizedExceptionMapper;
import uk.andrewgorton.digitalmarketplace.alerter.polling.DigitalMarketplacePoller;
import uk.andrewgorton.digitalmarketplace.alerter.polling.Fetcher;
import uk.andrewgorton.digitalmarketplace.alerter.resources.*;
import uk.andrewgorton.digitalmarketplace.alerter.security.SecurityService;
import uk.andrewgorton.digitalmarketplace.alerter.tasks.CreateNewUser;
import uk.andrewgorton.digitalmarketplace.alerter.tasks.GetHashedPasswordCommand;
import uk.andrewgorton.digitalmarketplace.alerter.tasks.SetUserPassword;
import uk.andrewgorton.digitalmarketplace.alerter.views.email.EmailViewRenderer;
import uk.andrewgorton.digitalmarketplace.alerter.views.email.ViewRendererLoader;

import java.util.ServiceLoader;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class DigitalMarketplaceAlerterApplication extends Application<DigitalMarketplaceAlerterConfiguration> {
    private final Logger LOGGER = LoggerFactory.getLogger(DigitalMarketplaceAlerterApplication.class);

    private final SecurityService securityService;

    public DigitalMarketplaceAlerterApplication() {
        this.securityService = new SecurityService();
    }

    public static void main(String[] args) throws Exception {
        new DigitalMarketplaceAlerterApplication().run(args);
    }

    @Override
    public String getName() {
        return "uk/andrewgorton/digitalmarketplace/alerter";
    }

    @Override
    public void initialize(Bootstrap<DigitalMarketplaceAlerterConfiguration> bootstrap) {
        bootstrap.addCommand(new GetHashedPasswordCommand());
        bootstrap.addCommand(new SetUserPassword(bootstrap.getApplication(), this.securityService));
        bootstrap.addCommand(new CreateNewUser(bootstrap.getApplication()));

        bootstrap.addBundle(new MigrationsBundle<DigitalMarketplaceAlerterConfiguration>() {
            @Override
            public DataSourceFactory getDataSourceFactory(DigitalMarketplaceAlerterConfiguration configuration) {
                return configuration.getDatabase();
            }
        });

        bootstrap.addBundle(new ViewBundle<>());
        bootstrap.addBundle(new AssetsBundle("/assets/", "/static/"));
    }

    @Override
    public void run(DigitalMarketplaceAlerterConfiguration configuration, Environment environment) {
        final DBIFactory factory = new DBIFactory();
        final DBI jdbi = factory.build(environment, configuration.getDatabase(), "h2");
        final OpportunityDAO opportunityDAO = jdbi.onDemand(OpportunityDAO.class);
        final ResponseDAO responseDAO = jdbi.onDemand(ResponseDAO.class);
        final AlertDAO alertDAO = jdbi.onDemand(AlertDAO.class);
        final UserDAO userDAO = jdbi.onDemand(UserDAO.class);
        final BidManagerDAO managerDAO = jdbi.onDemand(BidManagerDAO.class);
        final UserDAOFactory userDAOFactory = new UserDAOFactory(userDAO);
        final PieChartDAO pieChartDAO = jdbi.onDemand(PieChartDAO.class);
        final int pollPeriodMinutes = 30;

        environment.jersey().register(new AbstractBinder() {
            @Override
            protected void configure() {
                bindFactory(userDAOFactory).to(UserDAO.class)
                        .proxy(true).proxyForSameScope(false).in(RequestScoped.class);
            }
        });

        // Rename the session cookie identifier
        SessionHandler sh = new SessionHandler();
        sh.getSessionManager().getSessionCookieConfig().setName(configuration.getSessionCookieName());
        sh.getSessionManager().getSessionCookieConfig().setHttpOnly(true);
        environment.servlets().setSessionHandler(sh);

        environment.jersey().register(LoginRequiredFeature.class);
        environment.jersey().register(AdminRequiredFeature.class);
        environment.jersey().register(ForbiddenExceptionMapper.class);
        environment.jersey().register(UnauthorizedExceptionMapper.class);
        environment.jersey().register(IllegalArgumentExceptionMapper.class);

        // Email
        final EmailConfiguration emailConfiguration = configuration.getEmailConfiguration();
        final EmailViewRenderer emailViewRenderer =
                // ServiceLoader will load the currently active renderer (same code is in ViewBundle.java)
                new EmailViewRenderer(new ViewRendererLoader(ServiceLoader.load(ViewRenderer.class)));
        final EmailComposer emailComposer = new EmailComposer(emailConfiguration, emailViewRenderer);
        final EmailService emailService = new EmailService(emailComposer, emailConfiguration,
                EmailValidator.getInstance());

        // Resources
        environment.jersey().register(new HomepageResource(userDAO));
        environment.jersey().register(new OpportunityResource(opportunityDAO, responseDAO, emailService));
        environment.jersey().register(new AlertResource(alertDAO));
        environment.jersey().register(new UserResource(userDAO, this.securityService));
        environment.jersey().register(new BidManagerResource(managerDAO));
        environment.jersey().register(new SecurityResource(userDAO, emailService, this.securityService));
        environment.jersey().register(new ReportResource(pieChartDAO));

        // Opportunity polling
        final Fetcher fetcher = new Fetcher();
        final OpportunityFactory opportunityFactory = new OpportunityFactory();
        ScheduledExecutorService ses = environment.lifecycle().scheduledExecutorService("dma-%3d").build();
        ses.scheduleWithFixedDelay(
                new DigitalMarketplacePoller(
                        fetcher,
                        opportunityFactory,
                        opportunityDAO,
                        pollPeriodMinutes * 4),
                0,
                pollPeriodMinutes,
                TimeUnit.MINUTES);

        // Email Alerting
        final EmailAlerter emailAlerter =
                new EmailAlerter(emailConfiguration, emailComposer);
        final OpportunityToAlertMatcher opportunityToAlertMatcher = new OpportunityToAlertMatcher(opportunityDAO, alertDAO, emailAlerter);
        final EmailAlertRunner emailAlertRunner = new EmailAlertRunner(opportunityToAlertMatcher);
        ses.scheduleWithFixedDelay(emailAlertRunner, 0, 60, TimeUnit.SECONDS);
    }
}
