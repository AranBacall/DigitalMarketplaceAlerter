package uk.andrewgorton.digitalmarketplace.alerter.views.email.security;

import io.dropwizard.views.View;

/**
 * Represents an HTML template for a password reset email
 */
public class PasswordResetHtml extends View {

    private String url;

    public PasswordResetHtml(String url) {
        super("email-password-reset-html.ftl");
        this.url = url;
    }

    public String getUrl() {
        return url;
    }
}
