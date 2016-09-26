package uk.andrewgorton.digitalmarketplace.alerter.views;

import io.dropwizard.views.View;

import java.net.URI;

public class HomeView extends View {
    private final URI logoutLocation;

    public HomeView(URI logoutLocation) {
        super("homepage.ftl");
        this.logoutLocation = logoutLocation;
    }

    public URI getLogoutLocation() {
        return logoutLocation;
    }
}
