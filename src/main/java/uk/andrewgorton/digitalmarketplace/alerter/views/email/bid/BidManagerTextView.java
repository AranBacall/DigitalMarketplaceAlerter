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
    private List<Response> responses;
    private String adminName;
    private String adminEmail;


    public BidManagerTextView(Opportunity opportunity,
                              List<Response> responses,
                              String adminName, String adminEmail) {
        super("emailbidmanager-text.ftl");
        this.opportunity = opportunity;
        this.responses = responses;
        this.adminName = adminName;
        this.adminEmail = adminEmail;
    }

    public Opportunity getOpportunity() {
        return opportunity;
    }

    public List<Response> getResponses() {
        return responses;
    }

    public String getAdminName() {
        return adminName;
    }

    public String getAdminEmail() {
        return adminEmail;
    }
}
