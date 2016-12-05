package uk.andrewgorton.digitalmarketplace.alerter.mappers;

import uk.andrewgorton.digitalmarketplace.alerter.resources.SecurityResource;
import uk.andrewgorton.digitalmarketplace.alerter.views.exceptions.ForbiddenView;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.ForbiddenException;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

@Provider
public class ForbiddenExceptionMapper implements ExceptionMapper<ForbiddenException> {
    @Context
    UriInfo uriInfo;
    @Context
    HttpServletRequest request;

    @Override
    public Response toResponse(ForbiddenException e) {
        return Response
                .status(Response.Status.FORBIDDEN)
                .entity(
                        new ForbiddenView(
                                uriInfo.getBaseUriBuilder().path(SecurityResource.class).path("/login").build(),
                                request.getRequestURI(),
                                e.getMessage())
                ).build();
    }
}
