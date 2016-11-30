package uk.andrewgorton.digitalmarketplace.alerter.tasks;

import io.dropwizard.Application;
import io.dropwizard.cli.EnvironmentCommand;
import io.dropwizard.jdbi.DBIFactory;
import io.dropwizard.setup.Environment;
import net.sourceforge.argparse4j.inf.Namespace;
import net.sourceforge.argparse4j.inf.Subparser;
import org.skife.jdbi.v2.DBI;
import uk.andrewgorton.digitalmarketplace.alerter.DigitalMarketplaceAlerterConfiguration;
import uk.andrewgorton.digitalmarketplace.alerter.dao.UserDAO;
import uk.andrewgorton.digitalmarketplace.alerter.security.ProtectedPassword;
import uk.andrewgorton.digitalmarketplace.alerter.security.SecurityService;

public class SetUserPassword extends EnvironmentCommand<DigitalMarketplaceAlerterConfiguration> {

    private final SecurityService securityService;


    public SetUserPassword(Application<DigitalMarketplaceAlerterConfiguration> application,
                           SecurityService securityService) {
        super(application, "setuserpassword", "Sets the user password in the db");
        this.securityService = securityService;
    }

    @Override
    public void configure(Subparser subparser) {
        super.configure(subparser);

        subparser.addArgument("-u", "--username")
                .dest("username")
                .type(String.class)
                .required(true)
                .help("Username");
        subparser.addArgument("-p", "--password")
                .dest("password")
                .type(String.class)
                .required(true)
                .help("Password");
    }

    @Override
    protected void run(Environment environment, Namespace namespace, DigitalMarketplaceAlerterConfiguration digitalMarketplaceAlerterConfiguration) throws Exception {
        String username = namespace.getString("username");
        ProtectedPassword protectedPassword = this.securityService.protectPassword(namespace.getString("password"));

        final DBIFactory factory = new DBIFactory();
        final DBI jdbi = factory.build(environment, digitalMarketplaceAlerterConfiguration.getDatabase(), "h2");
        final UserDAO userDAO = jdbi.onDemand(UserDAO.class);

        userDAO.updateUserCredentials(username, protectedPassword.getSalt(), protectedPassword.getProtected());
        userDAO.close();
        System.out.println("Password Updated Successfully");

        // No idea why I have to do this but if I don't, the process hangs
        System.exit(0);
    }
}
