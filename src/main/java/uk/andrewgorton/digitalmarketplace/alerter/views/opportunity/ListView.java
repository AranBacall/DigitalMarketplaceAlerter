package uk.andrewgorton.digitalmarketplace.alerter.views.opportunity;

import io.dropwizard.views.View;
import uk.andrewgorton.digitalmarketplace.alerter.Opportunity;

import java.util.List;

public class ListView extends View {
    private List<Opportunity> opportunities;

    public ListView(List<Opportunity> opportunities) {
        super("listview.ftl");
        this.opportunities = opportunities;
    }

    public List<Opportunity> getOpportunities() {
        return opportunities;
    }
}
