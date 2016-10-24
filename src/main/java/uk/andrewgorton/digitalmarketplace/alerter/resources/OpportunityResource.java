package uk.andrewgorton.digitalmarketplace.alerter.resources;

import com.codahale.metrics.annotation.Timed;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import uk.andrewgorton.digitalmarketplace.alerter.annotations.LoginRequired;
import uk.andrewgorton.digitalmarketplace.alerter.dao.OpportunityDAO;
import uk.andrewgorton.digitalmarketplace.alerter.views.opportunity.DetailView;
import uk.andrewgorton.digitalmarketplace.alerter.views.opportunity.ListView;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.*;
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

    @Path("/{id}/duration")
    @POST
    @Timed
    @LoginRequired
    public Response updateOpportunityDuration(@PathParam("id") long id,
                                     @Context HttpServletRequest request,
                                     @Context UriInfo uriInfo,
                                     @FormParam("duration") String duration) {

        try {
            int durationInMonths = Integer.parseInt(duration);
            if(opportunityDAO.findById(id) != null)
            {
                opportunityDAO.setOpportunityDuration(durationInMonths,id);
                return Response.seeOther(
                        uriInfo.getBaseUriBuilder().path(OpportunityResource.class).segment(String.valueOf(id)).build())
                        .build();
            }
            else
            {
                return Response.status(Response.Status.BAD_REQUEST).build();
            }
        }catch(NumberFormatException nfe)
        {
            return Response.status(Response.Status.BAD_REQUEST).build();
        }
    }

    @Path("/{id}/cost")
    @POST
    @Timed
    @LoginRequired
    public Response updateOpportunityCost(@PathParam("id") long id,
                                              @Context HttpServletRequest request,
                                              @Context UriInfo uriInfo,
                                              @FormParam("cost") String cost) {

        try {
            int costInPounds = Integer.parseInt(cost);
            if(opportunityDAO.findById(id) != null)
            {
                opportunityDAO.setOpportunityCost(costInPounds,id);
                return Response.seeOther(
                        uriInfo.getBaseUriBuilder().path(OpportunityResource.class).segment(String.valueOf(id)).build())
                        .build();
            }
            else
            {
                return Response.status(Response.Status.BAD_REQUEST).build();
            }
        }catch(NumberFormatException nfe)
        {
            return Response.status(Response.Status.BAD_REQUEST).build();
        }
    }
}
