package uk.andrewgorton.digitalmarketplace.alerter.email;


import uk.andrewgorton.digitalmarketplace.alerter.Opportunity;

import java.text.SimpleDateFormat;

public class TextEmailBodyFactory {
    private final String ENDL = System.getProperty("line.separator");
    private final SimpleDateFormat sdf = new SimpleDateFormat("EEEE, d MMMM yyyy");
    private final String adminName;
    private final String adminEmail;

    public TextEmailBodyFactory(String adminName, String adminEmail) {
        this.adminName = adminName;
        this.adminEmail = adminEmail;
    }

    public String create(Opportunity o) {
        StringBuilder sbPlain = new StringBuilder();
        sbPlain.append(o.getTitle());
        if (!o.getTitle().endsWith(".")) {
            sbPlain.append(".");
        }
        sbPlain.append(ENDL);
        sbPlain.append(o.getCustomer());
        if (!o.getCustomer().endsWith(".")) {
            sbPlain.append(".");
        }
        sbPlain.append(ENDL);
        sbPlain.append(o.getLocation());
        if (!o.getLocation().endsWith(".")) {
            sbPlain.append(".");
        }
        sbPlain.append(ENDL);
        sbPlain.append(o.getOpportunityType());
        if (!o.getOpportunityType().endsWith(".")) {
            sbPlain.append(".");
        }
        sbPlain.append(ENDL);
        sbPlain.append(o.getExcerpt());
        if (!o.getExcerpt().endsWith(".")) {
            sbPlain.append(".");
        }
        sbPlain.append(ENDL);

        sbPlain.append(String.format("Closing: %s.", sdf.format(o.getClosing())));
        sbPlain.append(ENDL);
        sbPlain.append(o.getUrl());
        sbPlain.append(ENDL);
        sbPlain.append(ENDL);
        sbPlain.append("Please contact ");
        sbPlain.append(adminName);
        sbPlain.append(" (");
        sbPlain.append(adminEmail);
        sbPlain.append(") for any changes to your subscription.");
        return sbPlain.toString();
    }
}
