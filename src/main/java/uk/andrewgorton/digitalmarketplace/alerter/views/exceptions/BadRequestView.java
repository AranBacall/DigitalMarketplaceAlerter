package uk.andrewgorton.digitalmarketplace.alerter.views.exceptions;

import io.dropwizard.views.View;

public class BadRequestView extends View {
    private String exception;


    public BadRequestView(String exception) {
        super("badrequestview.ftl");
        this.exception = exception;
    }

    public String getException() {
        return exception;
    }

}
