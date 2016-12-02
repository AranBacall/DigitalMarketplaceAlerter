package uk.andrewgorton.digitalmarketplace.alerter.resources;

import com.codahale.metrics.annotation.Timed;
import io.dropwizard.jersey.sessions.Session;
import io.dropwizard.views.View;
import uk.andrewgorton.digitalmarketplace.alerter.User;
import uk.andrewgorton.digitalmarketplace.alerter.annotations.AdminRequired;
import uk.andrewgorton.digitalmarketplace.alerter.annotations.LoginRequired;
import uk.andrewgorton.digitalmarketplace.alerter.dao.UserDAO;
import uk.andrewgorton.digitalmarketplace.alerter.security.ProtectedPassword;
import uk.andrewgorton.digitalmarketplace.alerter.security.SecurityService;
import uk.andrewgorton.digitalmarketplace.alerter.views.UserListView;
import uk.andrewgorton.digitalmarketplace.alerter.views.security.ResetPasswordView;

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
    private final SecurityService securityService;

    public UserResource(UserDAO userDAO, SecurityService securityService) {
        this.userDAO = userDAO;
        this.securityService = securityService;
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

    @GET
    @Path("/{userid}/reset-password/{key}")
    @Timed
    public View showResetPasswordView(@PathParam("userid") long userId, @PathParam("key") String key) {
        User requestedUser = this.userDAO.findByKey(userId, key);
        if (requestedUser == null) {
            throw new ForbiddenException(String.format("No user / key combination exists with id %d and key %s",
                    userId, key));
        }
        return new ResetPasswordView();
    }

    @POST
    @Path("/{userid}/reset-password/{key}")
    @Timed
    public Response resetPassword(@Context UriInfo uriInfo, @PathParam("userid") long userId,
                                  @PathParam("key") String key,
                                  @FormParam("password") String barePassword,
                                  @FormParam("repeatPassword") String repeatPassword) {
        User requestedUser = this.userDAO.findByKey(userId, key);
        if (requestedUser == null) {
            return Response.status(Response.Status.BAD_REQUEST).entity(
                    String.format("No user / key combination exists with id %d and key %s", userId, key)).build();
        }

        if (!barePassword.equals(repeatPassword)) {
            return Response.status(Response.Status.BAD_REQUEST).entity(
                    String.format("Passwords %s and %s do not match", barePassword, repeatPassword)).build();
        }

        ProtectedPassword password = this.securityService.protectPassword(barePassword);
        this.userDAO.updateUserCredentials(requestedUser.getUsername(), password.getSalt(), password.getProtected());
        return Response.seeOther(uriInfo.getBaseUriBuilder()
                .path(SecurityResource.class)
                .path("/login")
                .build()
        ).build();
    }
}
