package uk.andrewgorton.digitalmarketplace.alerter.views.exceptions;

import io.dropwizard.views.View;

import java.net.URI;

public class ForbiddenView extends View {
    private final URI loginLocation;
    private final String returnLocation;

    public ForbiddenView(URI loginLocation, String returnLocation) {
        super("forbiddenview.ftl");
        this.loginLocation = loginLocation;
        this.returnLocation = returnLocation;
    }

    public URI getLoginLocation() {
        return loginLocation;
    }

    public String getReturnLocation() {
        return returnLocation;
    }
}
