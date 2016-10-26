package uk.andrewgorton.digitalmarketplace.alerter.views.bid;

import io.dropwizard.views.View;
import uk.andrewgorton.digitalmarketplace.alerter.model.BidManager;

import java.util.List;

public class BidManagerListView extends View {
    private List<BidManager> bidManagers;

    public BidManagerListView(List<BidManager> bidManagers) {
        super("bidmanagerlistview.ftl");
        this.bidManagers = bidManagers;
    }

    public List<BidManager> getBidManagers() {
        return bidManagers;
    }
}
