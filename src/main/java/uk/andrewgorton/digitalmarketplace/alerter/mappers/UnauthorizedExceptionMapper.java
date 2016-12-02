package uk.andrewgorton.digitalmarketplace.alerter.mappers;

import uk.andrewgorton.digitalmarketplace.alerter.exceptions.UnauthorizedException;
import uk.andrewgorton.digitalmarketplace.alerter.resources.HomepageResource;
import uk.andrewgorton.digitalmarketplace.alerter.views.exceptions.UnauthorizedView;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

@Provider
public class UnauthorizedExceptionMapper implements ExceptionMapper<UnauthorizedException> {
    @Context
    UriInfo uriInfo;
    @Context
    HttpServletRequest request;

    @Override
    public Response toResponse(UnauthorizedException e) {
        return Response
                .status(Response.Status.FORBIDDEN)
                .entity(
                        new UnauthorizedView(
                                uriInfo.getBaseUriBuilder().path(HomepageResource.class).build())
                ).build();
    }
}
