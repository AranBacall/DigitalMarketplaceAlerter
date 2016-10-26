package uk.andrewgorton.digitalmarketplace.alerter.resources;

import com.codahale.metrics.annotation.Timed;
import org.apache.commons.validator.routines.EmailValidator;
import uk.andrewgorton.digitalmarketplace.alerter.annotations.LoginRequired;
import uk.andrewgorton.digitalmarketplace.alerter.dao.BidManagerDAO;
import uk.andrewgorton.digitalmarketplace.alerter.views.bid.BidManagerListView;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriBuilder;
import javax.ws.rs.core.UriInfo;

@Path("/bidmanager")
public class BidManagerResource {
    private final BidManagerDAO bidManagerDAO;

    public BidManagerResource(BidManagerDAO bidManagerDAO) {
        this.bidManagerDAO = bidManagerDAO;
    }

    @GET
    @Timed
    @LoginRequired
    public Object getAll(@Context HttpServletRequest request) {
        if (!request.getRequestURI().endsWith("/")) {
            return Response.seeOther(UriBuilder.fromPath(request.getRequestURI() + "/").build())
                    .build();
        }

        return new BidManagerListView(bidManagerDAO.findAll());
    }

    @POST
    @Path("/{id}/delete")
    @Timed
    @LoginRequired
    public Response delete(@PathParam("id") long id,
                           @Context UriInfo uriInfo) {
        bidManagerDAO.delete(id);

        return Response.seeOther(
                uriInfo.getBaseUriBuilder().path(BidManagerResource.class).build())
                .build();
    }

    @POST
    @Path("/create")
    @Timed
    @LoginRequired
    public Response create(@FormParam("email") String email,
                           @Context UriInfo uriInfo) {
        EmailValidator ev = EmailValidator.getInstance();
        if (!ev.isValid(email)) {
            throw new RuntimeException("Invalid email");
        }

        bidManagerDAO.insert(email);
        return Response.seeOther(
                uriInfo.getBaseUriBuilder().path(BidManagerResource.class).build())
                .build();
    }
}
