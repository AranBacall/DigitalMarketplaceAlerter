package uk.andrewgorton.digitalmarketplace.alerter.resources;

import com.codahale.metrics.annotation.Timed;
import io.dropwizard.jersey.sessions.Session;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import uk.andrewgorton.digitalmarketplace.alerter.annotations.LoginRequired;
import uk.andrewgorton.digitalmarketplace.alerter.dao.UserDAO;
import uk.andrewgorton.digitalmarketplace.alerter.filters.AdminRequiredFilter;
import uk.andrewgorton.digitalmarketplace.alerter.views.HomeView;

import javax.servlet.http.HttpSession;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriInfo;
import java.net.URI;

@Path("/")
public class HomepageResource {
    private final Logger LOGGER = LoggerFactory.getLogger(HomepageResource.class);
    private final UserDAO userDAO;

    public HomepageResource(UserDAO userDAO) {
        this.userDAO = userDAO;
    }

    @GET
    @Timed
    @LoginRequired
    public Object getDefault(@Context UriInfo uriInfo, @Session HttpSession session) throws Exception {
        URI logoutLocation = uriInfo
                .getBaseUriBuilder()
                .path(SecurityResource.class)
                .path("/logout")
                .scheme(null)
                .build();

        URI alertsLocation = null;
        URI bidManagersLocation = null;
        URI usersLocation = null;

        if(AdminRequiredFilter.isAdmin(session,userDAO))
        {
            alertsLocation = uriInfo
                    .getBaseUriBuilder()
                    .path(AlertResource.class)
                    .scheme(null)
                    .build();

            bidManagersLocation = uriInfo
                    .getBaseUriBuilder()
                    .path(BidManagerResource.class)
                    .scheme(null)
                    .build();

            usersLocation = uriInfo
                    .getBaseUriBuilder()
                    .path(UserResource.class)
                    .scheme(null)
                    .build();
        }

        return new HomeView(logoutLocation,alertsLocation,bidManagersLocation,usersLocation);
    }
}
