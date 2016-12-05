package uk.andrewgorton.digitalmarketplace.alerter.views.exceptions;

import io.dropwizard.views.View;

import java.net.URI;

public class ForbiddenView extends View {
    private final URI loginLocation;
    private final String returnLocation;
    private final String exceptionMessage;

    public ForbiddenView(URI loginLocation, String returnLocation, String exceptionMessage) {
        super("forbiddenview.ftl");
        this.loginLocation = loginLocation;
        this.returnLocation = returnLocation;
        this.exceptionMessage = exceptionMessage;
    }

    public URI getLoginLocation() {
        return loginLocation;
    }

    public String getReturnLocation() {
        return returnLocation;
    }

    public String getExceptionMessage() {
        return exceptionMessage;
    }
}
