package uk.andrewgorton.digitalmarketplace.alerter.views.security;


import io.dropwizard.views.View;

public class LoginView extends View {
    private final String username;
    private final String returnLocation;
    private String forgottenPasswordLocation;

    public LoginView(String returnLocation, String forgottenPasswordLocation) {
        this(null, returnLocation, forgottenPasswordLocation);
    }

    public LoginView(String username, String returnLocation, String forgottenPasswordLocation) {
        super("loginview.ftl");
        this.username = username;
        this.returnLocation = returnLocation;
        this.forgottenPasswordLocation = forgottenPasswordLocation;
    }

    public String getUsername() {
        return username;
    }

    public String getReturnLocation() {
        return returnLocation;
    }

    public String getForgottenPasswordLocation() {
        return forgottenPasswordLocation;
    }
}
