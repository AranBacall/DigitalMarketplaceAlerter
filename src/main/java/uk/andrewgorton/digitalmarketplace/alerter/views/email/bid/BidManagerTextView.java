package uk.andrewgorton.digitalmarketplace.alerter.views.email.bid;

import io.dropwizard.views.View;
import uk.andrewgorton.digitalmarketplace.alerter.Opportunity;
import uk.andrewgorton.digitalmarketplace.alerter.model.Response;

import java.util.List;

/**
 * Represents an HTML template for a bid manager email
 */
public class BidManagerTextView extends View {

    private Opportunity opportunity;
    private String responseBaseUrl;

    public BidManagerTextView(Opportunity opportunity,
                              String responseBaseUrl) {
        super("emailbidmanager-html.ftl");
        this.opportunity = opportunity;
        this.responseBaseUrl = responseBaseUrl;
    }

    public Opportunity getOpportunity() {
        return opportunity;
    }

    public String getResponseBaseUrl() {
        return responseBaseUrl;
    }
}
