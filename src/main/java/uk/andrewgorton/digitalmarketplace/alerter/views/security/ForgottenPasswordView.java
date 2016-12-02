package uk.andrewgorton.digitalmarketplace.alerter.views.security;

import io.dropwizard.views.View;

/**
 * Created by ross on 28/11/16.
 */
public class ForgottenPasswordView extends View {

    private boolean submitted;


    public ForgottenPasswordView() {
        this(false);
    }

    public ForgottenPasswordView(boolean submitted) {
        super("forgotten-password-view.ftl");
        this.submitted = submitted;
    }

    public boolean isSubmitted() {
        return submitted;
    }
}
