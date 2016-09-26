package uk.andrewgorton.digitalmarketplace.alerter.resources;

import com.codahale.metrics.annotation.Timed;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import uk.andrewgorton.digitalmarketplace.alerter.annotations.LoginRequired;
import uk.andrewgorton.digitalmarketplace.alerter.dao.OpportunityDAO;
import uk.andrewgorton.digitalmarketplace.alerter.views.opportunity.DetailView;
import uk.andrewgorton.digitalmarketplace.alerter.views.opportunity.ListView;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriBuilder;
import javax.ws.rs.core.UriInfo;

@Path("/opportunity")
public class OpportunityResource {
    private final Logger LOGGER = LoggerFactory.getLogger(OpportunityResource.class);
    private final OpportunityDAO opportunityDAO;

    public OpportunityResource(OpportunityDAO opportunityDAO) {
        this.opportunityDAO = opportunityDAO;
    }

    @GET
    @Timed
    @LoginRequired
    public Object getAll(@Context HttpServletRequest request) {
        if (!request.getRequestURI().endsWith("/")) {
            return Response.seeOther(UriBuilder.fromPath(request.getRequestURI() + "/").build())
                    .build();
        }

        return new ListView(opportunityDAO.findAllOpen());
    }

    @Path("/{id}")
    @GET
    @Timed
    @LoginRequired
    public Object getOpportunityById(@PathParam("id") long id,
                                     @Context HttpServletRequest request) {
        if (!request.getRequestURI().endsWith("/")) {
            return Response.seeOther(UriBuilder.fromPath(request.getRequestURI() + "/").build())
                    .build();
        }

        return new DetailView(opportunityDAO.findById(id));
    }

    @Path("/{id}/clearAlert")
    @POST
    @Timed
    @LoginRequired
    public Response handleClearAlert(@PathParam("id") long id,
                                     @Context HttpServletRequest request,
                                     @Context UriInfo uriInfo) {
        opportunityDAO.markUnalerted(id);
        return Response.seeOther(
                uriInfo.getBaseUriBuilder().path(OpportunityResource.class).build())
                .build();
    }
}
