package uk.andrewgorton.digitalmarketplace.alerter.resources;

import com.codahale.metrics.annotation.Timed;
import org.apache.commons.validator.routines.EmailValidator;
import uk.andrewgorton.digitalmarketplace.alerter.annotations.LoginRequired;
import uk.andrewgorton.digitalmarketplace.alerter.dao.AlertDAO;
import uk.andrewgorton.digitalmarketplace.alerter.views.alert.ListView;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriBuilder;
import javax.ws.rs.core.UriInfo;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

@Path("/alert")
public class AlertResource {
    private final AlertDAO alertDAO;

    public AlertResource(AlertDAO alertDAO) {
        this.alertDAO = alertDAO;
    }

    @GET
    @Timed
    @LoginRequired
    public Object getAll(@Context HttpServletRequest request) {
        if (!request.getRequestURI().endsWith("/")) {
            return Response.seeOther(UriBuilder.fromPath(request.getRequestURI() + "/").build())
                    .build();
        }

        return new ListView(alertDAO.findAll());
    }

    @POST
    @Path("/{id}/enable")
    @Timed
    @LoginRequired
    public Response enableAlert(@PathParam("id") long id,
                                @Context UriInfo uriInfo) {
        alertDAO.enable(id);

        return Response.seeOther(
                uriInfo.getBaseUriBuilder().path(AlertResource.class).build())
                .build();
    }

    @POST
    @Path("/{id}/disable")
    @Timed
    @LoginRequired
    public Response disable(@PathParam("id") long id,
                            @Context UriInfo uriInfo) {
        alertDAO.disable(id);

        return Response.seeOther(
                uriInfo.getBaseUriBuilder().path(AlertResource.class).build())
                .build();
    }

    @POST
    @Path("/{id}/delete")
    @Timed
    @LoginRequired
    public Response delete(@PathParam("id") long id,
                           @Context UriInfo uriInfo) {
        alertDAO.delete(id);

        return Response.seeOther(
                uriInfo.getBaseUriBuilder().path(AlertResource.class).build())
                .build();
    }

    @POST
    @Path("/create")
    @Timed
    @LoginRequired
    public Response create(@FormParam("email") String email,
                           @FormParam("customerRegex") String customerMatchRegex,
                           @Context UriInfo uriInfo) {
        EmailValidator ev = EmailValidator.getInstance();
        if (!ev.isValid(email)) {
            throw new RuntimeException("Invalid email");
        }
        try {
            Pattern.compile(customerMatchRegex);
        } catch (PatternSyntaxException pse) {
            throw new RuntimeException(pse);
        }

        alertDAO.insert(email, customerMatchRegex, true);
        return Response.seeOther(
                uriInfo.getBaseUriBuilder().path(AlertResource.class).build())
                .build();
    }
}
