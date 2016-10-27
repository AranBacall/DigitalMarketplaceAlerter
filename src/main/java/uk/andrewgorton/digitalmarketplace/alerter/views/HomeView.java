package uk.andrewgorton.digitalmarketplace.alerter.views;

import io.dropwizard.views.View;

import java.net.URI;

public class HomeView extends View {

    private final URI logoutLocation;
    private final URI bidManagersLocation;
    private final URI usersLocation;


    public HomeView(URI logoutLocation, URI bidManagersLocation, URI usersLocation) {
        super("homepage.ftl");
        this.logoutLocation = logoutLocation;
        this.bidManagersLocation = bidManagersLocation;
        this.usersLocation = usersLocation;
    }

    public URI getLogoutLocation() {
        return logoutLocation;
    }


    public URI getBidManagersLocation() {
        return bidManagersLocation;
    }

    public URI getUsersLocation() {
        return usersLocation;
    }
}
