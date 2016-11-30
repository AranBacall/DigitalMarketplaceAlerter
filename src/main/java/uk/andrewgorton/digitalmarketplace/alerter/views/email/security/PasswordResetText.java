package uk.andrewgorton.digitalmarketplace.alerter.views.email.security;

import io.dropwizard.views.View;

/**
 * Represents a text template for a password reset email
 */
public class PasswordResetText extends View {

    private String url;

    public PasswordResetText(String url) {
        super("email-password-reset-text.ftl");
        this.url = url;
    }

    public String getUrl() {
        return url;
    }
}
