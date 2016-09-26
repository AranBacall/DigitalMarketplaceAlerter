package uk.andrewgorton.digitalmarketplace.alerter.functional;

import io.dropwizard.client.JerseyClientBuilder;
import io.dropwizard.server.SimpleServerFactory;
import io.dropwizard.testing.junit.DropwizardAppRule;
import org.glassfish.jersey.client.ClientProperties;
import org.junit.BeforeClass;
import org.junit.ClassRule;
import org.junit.Test;
import uk.andrewgorton.digitalmarketplace.alerter.DigitalMarketplaceAlerterApplication;
import uk.andrewgorton.digitalmarketplace.alerter.DigitalMarketplaceAlerterConfiguration;
import uk.andrewgorton.digitalmarketplace.alerter.resources.AlertResource;
import uk.andrewgorton.digitalmarketplace.alerter.resources.OpportunityResource;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriBuilder;

import static io.dropwizard.testing.ResourceHelpers.resourceFilePath;
import static org.assertj.core.api.Assertions.assertThat;

public class FunctionalTest {
    @ClassRule
    public static final DropwizardAppRule<DigitalMarketplaceAlerterConfiguration> RULE =
            new DropwizardAppRule<>(DigitalMarketplaceAlerterApplication.class, resourceFilePath("testConfiguration.yml"));

    private static Client client;

    @BeforeClass
    public static void setup() {
        client = new JerseyClientBuilder(RULE.getEnvironment()).build("test client");
        client.property(ClientProperties.CONNECT_TIMEOUT, "10000");
        client.property(ClientProperties.READ_TIMEOUT, "5000");
    }

    @Test
    public void CantViewHomepageWithoutAuthenticatedSession() {
        String requestedPath = String.format("http://localhost:%d/%s",
                RULE.getLocalPort(),
                ((SimpleServerFactory) RULE.getConfiguration().getServerFactory()).getApplicationContextPath());

        Response response = client
                .target(requestedPath)
                .request()
                .get();

        assertThat(response.getStatus()).isEqualTo(403);
    }

    @Test
    public void CantViewOppoortunitiesWithoutAuthenticatedSession() {
        String requestedPath = String.format("http://localhost:%d%s%s",
                RULE.getLocalPort(),
                ((SimpleServerFactory) RULE.getConfiguration().getServerFactory()).getApplicationContextPath(),
                UriBuilder.fromResource(OpportunityResource.class).build());
        System.out.println(requestedPath);
        Response response = client
                .target(requestedPath)
                .request()
                .get();

        assertThat(response.getStatus()).isEqualTo(403);
    }

    @Test
    public void CantGetOppoortunityWithoutAuthenticatedSession() {
        String requestedPath = String.format("http://localhost:%d%s%s/1",
                RULE.getLocalPort(),
                ((SimpleServerFactory) RULE.getConfiguration().getServerFactory()).getApplicationContextPath(),
                UriBuilder.fromResource(OpportunityResource.class).build());
        System.out.println(requestedPath);
        Response response = client
                .target(requestedPath)
                .request()
                .get();

        assertThat(response.getStatus()).isEqualTo(403);
    }

    @Test
    public void CantClearOppoortunityAlertWithoutAuthenticatedSession() {
        String requestedPath = String.format("http://localhost:%d%s%s/1/clearAlert",
                RULE.getLocalPort(),
                ((SimpleServerFactory) RULE.getConfiguration().getServerFactory()).getApplicationContextPath(),
                UriBuilder.fromResource(OpportunityResource.class).build());
        System.out.println(requestedPath);
        Response response = client
                .target(requestedPath)
                .request()
                .post(Entity.json("Blargh"));

        assertThat(response.getStatus()).isEqualTo(403);
    }

    @Test
    public void CantViewAlertsWithoutAuthenticatedSession() {
        String requestedPath = String.format("http://localhost:%d%s%s",
                RULE.getLocalPort(),
                ((SimpleServerFactory) RULE.getConfiguration().getServerFactory()).getApplicationContextPath(),
                UriBuilder.fromResource(AlertResource.class).build());
        System.out.println(requestedPath);
        Response response = client
                .target(requestedPath)
                .request()
                .get();

        assertThat(response.getStatus()).isEqualTo(403);
    }

    @Test
    public void CantEnableAlertWithoutAuthenticatedSession() {
        String requestedPath = String.format("http://localhost:%d%s%s/1/enable",
                RULE.getLocalPort(),
                ((SimpleServerFactory) RULE.getConfiguration().getServerFactory()).getApplicationContextPath(),
                UriBuilder.fromResource(AlertResource.class).build());
        System.out.println(requestedPath);
        Response response = client
                .target(requestedPath)
                .request()
                .post(Entity.json("Random text"));

        assertThat(response.getStatus()).isEqualTo(403);
    }

    @Test
    public void CantDisableAlertWithoutAuthenticatedSession() {
        String requestedPath = String.format("http://localhost:%d%s%s/1/disable",
                RULE.getLocalPort(),
                ((SimpleServerFactory) RULE.getConfiguration().getServerFactory()).getApplicationContextPath(),
                UriBuilder.fromResource(AlertResource.class).build());
        System.out.println(requestedPath);
        Response response = client
                .target(requestedPath)
                .request()
                .post(Entity.json("Random text"));

        assertThat(response.getStatus()).isEqualTo(403);
    }

    @Test
    public void CantDeleteAlertWithoutAuthenticatedSession() {
        String requestedPath = String.format("http://localhost:%d%s%s/1/delete",
                RULE.getLocalPort(),
                ((SimpleServerFactory) RULE.getConfiguration().getServerFactory()).getApplicationContextPath(),
                UriBuilder.fromResource(AlertResource.class).build());
        System.out.println(requestedPath);
        Response response = client
                .target(requestedPath)
                .request()
                .post(Entity.json("Random text"));

        assertThat(response.getStatus()).isEqualTo(403);
    }

    @Test
    public void CantCreateAlertWithoutAuthenticatedSession() {
        String requestedPath = String.format("http://localhost:%d%s%s/create",
                RULE.getLocalPort(),
                ((SimpleServerFactory) RULE.getConfiguration().getServerFactory()).getApplicationContextPath(),
                UriBuilder.fromResource(AlertResource.class).build());
        System.out.println(requestedPath);
        Response response = client
                .target(requestedPath)
                .request()
                .post(Entity.json("Random text"));

        assertThat(response.getStatus()).isEqualTo(403);
    }
}
