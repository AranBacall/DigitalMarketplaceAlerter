package uk.andrewgorton.digitalmarketplace.alerter.mappers;

import uk.andrewgorton.digitalmarketplace.alerter.views.exceptions.BadRequestView;

import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;

/**
 * Maps exceptions thrown by resources and turns them into a web friendly response.
 */
public class IllegalArgumentExceptionMapper implements ExceptionMapper<IllegalArgumentException> {


    @Override
    public Response toResponse(IllegalArgumentException e) {
        return Response
                .status(Response.Status.BAD_REQUEST)
                .entity(new BadRequestView(e.getMessage()))
                .build();
    }
}
