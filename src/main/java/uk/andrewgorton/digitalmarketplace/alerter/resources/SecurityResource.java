package uk.andrewgorton.digitalmarketplace.alerter.resources;

import com.codahale.metrics.annotation.Timed;
import io.dropwizard.jersey.sessions.Session;
import io.dropwizard.views.View;
import org.apache.commons.codec.binary.Hex;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import uk.andrewgorton.digitalmarketplace.alerter.User;
import uk.andrewgorton.digitalmarketplace.alerter.dao.UserDAO;
import uk.andrewgorton.digitalmarketplace.alerter.views.security.LoginView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriBuilder;
import javax.ws.rs.core.UriInfo;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;

@Path("/security")
public class SecurityResource {
    private final Logger LOGGER = LoggerFactory.getLogger(SecurityResource.class);
    private final UserDAO userDAO;

    public SecurityResource(UserDAO userDAO) {
        this.userDAO = userDAO;
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
        User u = null;
        if (StringUtils.isNotBlank(username)) {
            u = userDAO.findByUsername(username);
        }
        if (u != null && !u.isDisabled()) {
            String passwordGuess = String.format("%s%s", u.getSalt(), password);
            try {
                MessageDigest digest = MessageDigest.getInstance("SHA-256");
                byte[] hash = digest.digest(passwordGuess.getBytes(StandardCharsets.UTF_8));
                String hexHash = Hex.encodeHexString(hash);
                if (hexHash.compareToIgnoreCase(u.getPassword()) == 0) {
                    session.setAttribute("username", u.getUsername());
                    session.setAttribute("userid", u.getId());
                    session.setAttribute("authenticated", true);

                    if (req.getHeader("X-Forwarded-For") != null) {
                        LOGGER.info(String.format("Successful login for %s from ip %s (%s)", u.getUsername(),
                                req.getHeader("X-Forwarded-For"),
                                req.getRemoteHost()));
                    } else {
                        LOGGER.info(String.format("Successful login for %s from ip %s",
                                u.getUsername(), req.getRemoteHost()));
                    }
                    if (StringUtils.isNotBlank(returnLocation)) {
                        return Response.seeOther(UriBuilder.fromUri(returnLocation).build()).build();
                    } else {
                        return Response.seeOther(uriInfo.getBaseUriBuilder().path(HomepageResource.class).build()).build();
                    }
                }
            } catch (Exception e) {
                LOGGER.error(e.getMessage(), e);
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

    @POST
    @Path("/logout")
    @Timed
    public Response handleLogout(@Session HttpSession session,
                                 @Context UriInfo uriInfo) {
        session.setAttribute("authenticated", false);
        return Response.seeOther(uriInfo.getBaseUriBuilder().path(SecurityResource.class).path("/login").build()).build();
    }
}
