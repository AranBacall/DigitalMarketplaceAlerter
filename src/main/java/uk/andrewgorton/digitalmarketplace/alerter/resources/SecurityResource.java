package uk.andrewgorton.digitalmarketplace.alerter.resources;

import com.codahale.metrics.annotation.Timed;
import io.dropwizard.jersey.sessions.Session;
import io.dropwizard.views.View;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.mail.EmailException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import uk.andrewgorton.digitalmarketplace.alerter.User;
import uk.andrewgorton.digitalmarketplace.alerter.dao.UserDAO;
import uk.andrewgorton.digitalmarketplace.alerter.email.EmailService;
import uk.andrewgorton.digitalmarketplace.alerter.security.SecurityService;
import uk.andrewgorton.digitalmarketplace.alerter.views.security.ForgottenPasswordView;
import uk.andrewgorton.digitalmarketplace.alerter.views.security.LoginView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriBuilder;
import javax.ws.rs.core.UriInfo;
import java.util.UUID;

@Path("/security")
public class SecurityResource {
    private final Logger LOGGER = LoggerFactory.getLogger(SecurityResource.class);
    private final UserDAO userDAO;
    private final EmailService emailService;
    private final SecurityService securityService;

    public SecurityResource(UserDAO userDAO, EmailService emailService, SecurityService securityService) {
        this.userDAO = userDAO;
        this.emailService = emailService;
        this.securityService = securityService;
    }

    @GET
    @Path("/login")
    @Timed
    public View showLoginForm(@QueryParam("returnLocation") String returnLocation) {
        return new LoginView(returnLocation);
    }

    @POST
    @Path("/login")
    @Timed
    public Response handleLogin(@Session HttpSession session,
                                @Context HttpServletRequest req,
                                @Context UriInfo uriInfo,
                                @QueryParam("returnLocation") String returnLocation,
                                @FormParam("Username") String username,
                                @FormParam("Password") String password) throws Exception {
        User user = userDAO.findByUsername(username);

        if (user == null) {
            throw new ForbiddenException("The username is not recognised");
        }

        if (user.isDisabled()) {
            throw new ForbiddenException(String.format("User %s has been disabled, please contact your administrator",
                    user.getUsername()));
        }

        if (this.securityService.verifyPassword(user, password)) {
            session.setAttribute("username", user.getUsername());
            session.setAttribute("userid", user.getId());
            session.setAttribute("authenticated", true);

            if (req.getHeader("X-Forwarded-For") != null) {
                LOGGER.info(String.format("Successful login for %s from ip %s (%s)", user.getUsername(),
                        req.getHeader("X-Forwarded-For"),
                        req.getRemoteHost()));
            } else {
                LOGGER.info(String.format("Successful login for %s from ip %s",
                        user.getUsername(), req.getRemoteHost()));
            }
            if (StringUtils.isNotBlank(returnLocation)) {
                return Response.seeOther(UriBuilder.fromUri(returnLocation).build()).build();
            } else {
                return Response.seeOther(uriInfo.getBaseUriBuilder().path(HomepageResource.class).build()).build();
            }
        }

        // Log an error message
        StringBuilder sb = new StringBuilder();
        sb.append("Failed login");
        if (StringUtils.isNotBlank(username)) {
            sb.append(String.format(" for user '%s'", username));
        }
        if (req.getHeader("X-Forwarded-For") != null) {
            sb.append(String.format(" from ip %s (%s)", req.getHeader("X-Forwarded-For"), req.getRemoteHost()));
        } else {
            sb.append(String.format(" from ip %s", req.getRemoteHost()));
        }
        LOGGER.info(sb.toString());

        // Sleep to avoid hack attack
        Thread.sleep(1000);
        session.setAttribute("username", username);
        session.setAttribute("authenticated", false);
        return Response.seeOther(
                uriInfo.getBaseUriBuilder()
                        .path(SecurityResource.class)
                        .path("/login")
                        .queryParam("returnLocation", returnLocation)
                        .build()
        ).build();
    }

    @GET
    @Path("/forgotten-password")
    public View showPasswordChangeLandingForm() {
        return new ForgottenPasswordView();
    }

    @POST
    @Path("/forgotten-password")
    public Response verifyUserAndSendPasswordResetEmail(@Context UriInfo uriInfo, @FormParam("email") String email) {
        User found = this.userDAO.findByEmail(email);

        if (found == null) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity(String.format("No user found with email address %s", email)).build();
        }

        long id = found.getId();
        String key = UUID.randomUUID().toString();
        String url = uriInfo.getBaseUriBuilder()
                .path(UserResource.class)
                .path(Long.toString(id))
                .path("/reset-password")
                .path(key)
                .build().toString();
        this.userDAO.insertKey(id, key);
        try {
            this.emailService.sendPasswordResetEmail(found, url);
        } catch (EmailException e) {
            LOGGER.error("Failed to send password reset email", e);
            return Response.serverError().entity(e.getMessage()).build();
        }

        return Response.status(Response.Status.OK)
                .entity(String.format("A password reset email has been sent to %s\n" +
                        "Please close this page", found.getEmail())).build();
    }

    @POST
    @Path("/logout")
    @Timed
    public Response handleLogout(@Session HttpSession session,
                                 @Context UriInfo uriInfo) {
        session.setAttribute("authenticated", false);
        return Response.seeOther(uriInfo.getBaseUriBuilder().path(SecurityResource.class).path("/login").build()).build();
    }
}
