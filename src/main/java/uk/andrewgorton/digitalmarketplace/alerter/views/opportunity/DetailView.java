package uk.andrewgorton.digitalmarketplace.alerter.views.opportunity;

import io.dropwizard.views.View;
import uk.andrewgorton.digitalmarketplace.alerter.Opportunity;

public class DetailView extends View {
    private Opportunity opportunity;

    public DetailView(Opportunity opportunity) {
        super("detailview.ftl");
        this.opportunity = opportunity;
    }

    public Opportunity getOpportunity() {
        return opportunity;
    }
}
