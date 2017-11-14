package uk.andrewgorton.digitalmarketplace.alerter.resources;

import com.codahale.metrics.annotation.Timed;
import io.dropwizard.jersey.sessions.Session;
import uk.andrewgorton.digitalmarketplace.alerter.filters.AdminRequiredFilter;
import uk.andrewgorton.digitalmarketplace.alerter.views.HomeView;
import uk.andrewgorton.digitalmarketplace.alerter.views.SubscriptionView;

import javax.servlet.http.HttpSession;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriInfo;
import java.net.URI;

/**
 * Created by callumbarnes on 13/11/2017.
 */
@Path("/subscription")
public class SubscriptionResource {

    public SubscriptionResource() {

    }

    @GET
    @Timed
    public Object getDefault(@Context UriInfo uriInfo, @Session HttpSession session) throws Exception {
        URI loginLocation = uriInfo
                .getBaseUriBuilder()
                .path(SecurityResource.class)
                .path("/login")
                .scheme(null)
                .build();

        return new SubscriptionView(loginLocation);
    }

}
