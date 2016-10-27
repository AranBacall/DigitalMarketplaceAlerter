package uk.andrewgorton.digitalmarketplace.alerter.resources;

import com.codahale.metrics.annotation.Timed;
import io.dropwizard.jersey.sessions.Session;
import uk.andrewgorton.digitalmarketplace.alerter.User;
import uk.andrewgorton.digitalmarketplace.alerter.annotations.AdminRequired;
import uk.andrewgorton.digitalmarketplace.alerter.annotations.LoginRequired;
import uk.andrewgorton.digitalmarketplace.alerter.dao.UserDAO;
import uk.andrewgorton.digitalmarketplace.alerter.views.UserListView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriBuilder;
import javax.ws.rs.core.UriInfo;


@Path("/user")
public class UserResource {

    private final UserDAO userDAO;

    public UserResource(UserDAO userDAO) {
        this.userDAO = userDAO;
    }

    @GET
    @Timed
    @LoginRequired
    @AdminRequired
    public Object getAllUsers(@Context HttpServletRequest request) {
        if (!request.getRequestURI().endsWith("/")) {
            return Response.seeOther(UriBuilder.fromPath(request.getRequestURI() + "/").build())
                    .build();
        }
        return new UserListView(userDAO.findAll());
    }

    @POST
    @Path("/{userid}/delete")
    @Timed
    @LoginRequired
    @AdminRequired
    public Response delete(@PathParam("userid") long userid,
                           @Context UriInfo uriInfo) {
        userDAO.delete(userid);

        return Response.seeOther(
                uriInfo.getBaseUriBuilder().path(UserResource.class).build())
                .build();
    }

    @POST
    @Path("/{userid}/admin")
    @Timed
    @LoginRequired
    @AdminRequired
    public Response promoteToAdmin(@Context HttpServletRequest request, @Session HttpSession session, @Context UriInfo uriInfo, @PathParam("userid") long userid) {

        User requestedUser = userDAO.findById(userid);
        if(requestedUser != null)
        {
            if(!requestedUser.isDisabled())
            {
                userDAO.promoteUserToAdministrator(userid);
                return Response.seeOther(
                        uriInfo.getBaseUriBuilder().path(UserResource.class).build())
                        .build();
            }
            else
            {
                return Response.status(Response.Status.BAD_REQUEST).build();
            }
        }
        else {
            return Response.status(Response.Status.BAD_REQUEST).build();
        }
    }

    @POST
    @Path("/{userid}/disable/{enablement}")
    @Timed
    @LoginRequired
    @AdminRequired
    public Response updateUserEnablement(@Context HttpServletRequest request, @Session HttpSession session, @Context UriInfo uriInfo, @PathParam("userid") long userid, @PathParam("enablement") boolean enablement) {

        User requestedUser = userDAO.findById(userid);
        if(requestedUser != null)
        {
            //
            userDAO.updateUserEnablement(userid,enablement);
            return Response.seeOther(
                    uriInfo.getBaseUriBuilder().path(UserResource.class).build())
                    .build();
        }
        else
        {
            return Response.status(Response.Status.BAD_REQUEST).build();
        }
    }
}
