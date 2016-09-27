package uk.andrewgorton.digitalmarketplace.alerter.resources;

import com.codahale.metrics.annotation.Timed;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import uk.andrewgorton.digitalmarketplace.alerter.annotations.LoginRequired;
import uk.andrewgorton.digitalmarketplace.alerter.views.HomeView;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriInfo;
import java.net.URI;

@Path("/")
public class HomepageResource {
    private final Logger LOGGER = LoggerFactory.getLogger(HomepageResource.class);

    public HomepageResource() {

    }

    @GET
    @Timed
    @LoginRequired
    public Object getDefault(@Context UriInfo uriInfo) throws Exception {
        URI logoutLocation = uriInfo
                .getBaseUriBuilder()
                .path(SecurityResource.class)
                .path("/logout")
                .scheme(null)
                .build();
        return new HomeView(logoutLocation);
    }
}
