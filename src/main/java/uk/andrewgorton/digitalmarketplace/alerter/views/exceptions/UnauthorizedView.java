package uk.andrewgorton.digitalmarketplace.alerter.views.exceptions;

import io.dropwizard.views.View;

import java.net.URI;

public class UnauthorizedView extends View {
    private final URI homeLocation;

    public UnauthorizedView(URI homeLocation) {
        super("unauthorizedview.ftl");
        this.homeLocation = homeLocation;
    }

    public URI getHomeLocation() {
        return homeLocation;
    }

}
