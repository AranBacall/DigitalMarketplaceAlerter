package uk.andrewgorton.digitalmarketplace.alerter.views.alert;

import io.dropwizard.views.View;
import uk.andrewgorton.digitalmarketplace.alerter.Alert;

import java.util.List;

public class ListView extends View {
    private List<Alert> alerts;

    public ListView(List<Alert> alerts) {
        super("listview.ftl");
        this.alerts = alerts;
    }

    public List<Alert> getAlerts() {
        return alerts;
    }
}
