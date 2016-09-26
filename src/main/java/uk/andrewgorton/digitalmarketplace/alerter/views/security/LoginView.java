package uk.andrewgorton.digitalmarketplace.alerter.views.security;


import io.dropwizard.views.View;

public class LoginView extends View {
    private final String username;
    private final String returnLocation;

    public LoginView(String returnLocation) {
        super("loginview.ftl");
        this.username = null;
        this.returnLocation = returnLocation;
    }

    public LoginView(String username, String returnLocation) {
        super("loginview.ftl");
        this.username = username;
        this.returnLocation = returnLocation;
    }

    public String getUsername() {
        return username;
    }

    public String getReturnLocation() {
        return returnLocation;
    }
}
