package uk.andrewgorton.digitalmarketplace.alerter.resources;

import com.codahale.metrics.annotation.Timed;
import org.apache.commons.mail.EmailException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import uk.andrewgorton.digitalmarketplace.alerter.Opportunity;
import uk.andrewgorton.digitalmarketplace.alerter.annotations.LoginRequired;
import uk.andrewgorton.digitalmarketplace.alerter.dao.OpportunityDAO;
import uk.andrewgorton.digitalmarketplace.alerter.dao.ResponseDAO;
import uk.andrewgorton.digitalmarketplace.alerter.email.EmailService;
import uk.andrewgorton.digitalmarketplace.alerter.views.opportunity.DetailView;
import uk.andrewgorton.digitalmarketplace.alerter.views.opportunity.ListView;
import uk.andrewgorton.digitalmarketplace.alerter.views.response.ResponseExitView;
import uk.andrewgorton.digitalmarketplace.alerter.views.response.ResponseListView;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriBuilder;
import javax.ws.rs.core.UriInfo;
import java.net.URI;
import java.util.UUID;

@Path("/opportunity")
public class OpportunityResource {
    private final Logger LOGGER = LoggerFactory.getLogger(OpportunityResource.class);
    private final OpportunityDAO opportunityDAO;
    private final ResponseDAO responseDAO;
    private final EmailService emailService;

    public OpportunityResource(OpportunityDAO opportunityDAO,
                               ResponseDAO responseDAO, EmailService emailService) {
        this.opportunityDAO = opportunityDAO;
        this.responseDAO = responseDAO;
        this.emailService = emailService;
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

    @Path("/{id}/bidmanager")
    @POST
    @Timed
    @LoginRequired
    public Response emailBidManagers(@Context UriInfo uriInfo,
                                     @PathParam("id") long id,
                                     @FormParam("emails") String emailList) {
        URI opportunityBaseUri = uriInfo.getBaseUriBuilder().path(OpportunityResource.class).build();
        Opportunity opportunity = opportunityDAO.findById(id);
        if (opportunity == null) {
            return Response.status(Response.Status.BAD_REQUEST).build();
        }

        try {
            String key = UUID.randomUUID().toString();
            String responseUrl = String.format("%s/%s/response/%s", opportunityBaseUri,
                    opportunity.getId(), key);
            emailService.sendBidManagerEmail(opportunity, emailList.split(","), responseUrl);
            opportunityDAO.insertKey(id, key);
            return Response.seeOther(opportunityBaseUri).build();
        } catch (EmailException e) {
            LOGGER.error("Failed to send bid manager email", e);
            return Response.serverError().entity(e.getMessage()).build();
        }
    }

    @Path("{id}/response/{key}")
    @GET
    @Timed
    public ResponseListView responseHandling(@PathParam("id") Long opportunityId,
                                             @PathParam("key") String key) {
        if (opportunityDAO.findKey(opportunityId, key) == 0) {
            throw new ForbiddenException();
        }
        return new ResponseListView(opportunityId, responseDAO.findAll());
    }

    @Path("{id}/response/{key}")
    @POST
    @Timed
    public ResponseExitView responseHandling(@PathParam("id") Long opportunityId,
                                             @PathParam("key") String key,
                                             @FormParam("response") Long responseId) {
        if (opportunityDAO.findKey(opportunityId, key) == 0) {
            throw new ForbiddenException();
        }
        responseDAO.link(opportunityId, responseId);
        return new ResponseExitView();
    }
}
