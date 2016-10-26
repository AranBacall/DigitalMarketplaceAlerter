package uk.andrewgorton.digitalmarketplace.alerter.views.email.alert;

import io.dropwizard.views.View;
import uk.andrewgorton.digitalmarketplace.alerter.Opportunity;

/**
 * Represents an HTML template for an email alert
 */
public class AlertTextView extends View {

    private Opportunity opportunity;
    private String adminName;
    private String adminEmail;


    public AlertTextView(Opportunity opportunity, String adminName, String adminEmail) {
        super("emailalert-text.ftl");
        this.opportunity = opportunity;
        this.adminName = adminName;
        this.adminEmail = adminEmail;
    }

    public Opportunity getOpportunity() {
        return opportunity;
    }

    public String getAdminName() {
        return adminName;
    }

    public String getAdminEmail() {
        return adminEmail;
    }
}
