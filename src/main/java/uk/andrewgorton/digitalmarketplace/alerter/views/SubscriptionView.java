package uk.andrewgorton.digitalmarketplace.alerter.views;

import io.dropwizard.views.View;

import java.net.URI;

/**
 * Created by callumbarnes on 13/11/2017.
 */
public class SubscriptionView extends View {

    private URI loginLocation;

    public SubscriptionView(URI loginLocation)
    {
        super("subscription.ftl");
        this.loginLocation = loginLocation;
    }

    public URI getLoginLocation() {
        return loginLocation;
    }
}
