package uk.andrewgorton.digitalmarketplace.alerter.email;

import io.dropwizard.views.View;
import org.apache.commons.mail.EmailException;
import org.apache.commons.mail.HtmlEmail;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import uk.andrewgorton.digitalmarketplace.alerter.Opportunity;
import uk.andrewgorton.digitalmarketplace.alerter.User;
import uk.andrewgorton.digitalmarketplace.alerter.views.email.EmailViewRenderer;

import java.io.IOException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Tests for {@link EmailComposer}
 */
@RunWith(MockitoJUnitRunner.class)
public class EmailComposerTest {

    @Mock
    private EmailConfiguration configuration;
    @Mock
    private EmailViewRenderer renderer;
    @Mock
    private Opportunity opportunity;

    @Test
    public void ComposeAlert() throws Exception {
        String host = "host";
        int port = 1;
        String from = "nobody@nowhere.com";
        String customer = "customer";
        String htmlContent = "htmlMsg";
        String textContent = "txtMsg";

        when(configuration.getHost()).thenReturn(host);
        when(configuration.getPort()).thenReturn(port);
        when(configuration.getFrom()).thenReturn(from);
        when(opportunity.getCustomer()).thenReturn(customer);
        when(renderer.renderEmail(any(View.class)))
                .thenReturn(htmlContent)
                .thenReturn(textContent);
        EmailComposer composer = new EmailComposer(configuration, renderer);

        HtmlEmail email = composer.composeAlert(opportunity);

        assertEquals(host, email.getHostName());
        assertEquals(Integer.toString(port), email.getSmtpPort());
        assertEquals(from, email.getFromAddress().toString());
        assertTrue(email.getSubject().contains(customer));
    }

    @Test(expected = EmailException.class)
    public void FailToRender() throws Exception {
        String host = "host";
        int port = 1;
        String from = "nobody@nowhere.com";
        String customer = "customer";

        when(configuration.getHost()).thenReturn(host);
        when(configuration.getPort()).thenReturn(port);
        when(configuration.getFrom()).thenReturn(from);
        when(opportunity.getCustomer()).thenReturn(customer);
        when(renderer.renderEmail(any(View.class)))
                .thenThrow(new IOException("expected-error"));
        EmailComposer composer = new EmailComposer(configuration, renderer);

        composer.composeAlert(opportunity);
    }

    @Test
    public void ComposeBidManagerEmail() throws Exception {
        String host = "host";
        int port = 1;
        String from = "nobody@nowhere.com";
        String customer = "customer";
        String htmlContent = "htmlMsg";
        String textContent = "txtMsg";
        String responseBaseUrl = "responseUrl";

        when(configuration.getHost()).thenReturn(host);
        when(configuration.getPort()).thenReturn(port);
        when(configuration.getFrom()).thenReturn(from);
        when(opportunity.getCustomer()).thenReturn(customer);
        when(renderer.renderEmail(any(View.class)))
                .thenReturn(htmlContent)
                .thenReturn(textContent);
        EmailComposer composer = new EmailComposer(configuration, renderer);

        HtmlEmail email = composer.composeBidManagerEmail(opportunity, responseBaseUrl);

        assertEquals(host, email.getHostName());
        assertEquals(Integer.toString(port), email.getSmtpPort());
        assertEquals(from, email.getFromAddress().toString());
        assertTrue(email.getSubject().contains(customer));
    }

    @Test
    public void ComposePasswordResetEmail() throws Exception {
        String host = "host";
        int port = 1;
        String from = "nobody@nowhere.com";
        String htmlContent = "htmlMsg";
        String textContent = "txtMsg";
        String responseBaseUrl = "responseUrl";
        User user = mock(User.class);
        String recipient = "user@example.com";

        when(configuration.getHost()).thenReturn(host);
        when(configuration.getPort()).thenReturn(port);
        when(configuration.getFrom()).thenReturn(from);
        when(user.getEmail()).thenReturn(recipient);
        when(renderer.renderEmail(any(View.class)))
                .thenReturn(htmlContent)
                .thenReturn(textContent);
        EmailComposer composer = new EmailComposer(configuration, renderer);

        HtmlEmail email = composer.composePasswordResetEmail(user, responseBaseUrl);

        assertEquals(host, email.getHostName());
        assertEquals(Integer.toString(port), email.getSmtpPort());
        assertEquals(from, email.getFromAddress().toString());
        assertTrue(email.getSubject().equals("Password Reset"));
        assertEquals(1, email.getToAddresses().size());
        assertEquals(recipient, email.getToAddresses().get(0).getAddress());
    }

    @Test(expected = EmailException.class)
    public void FailToRenderPasswordResetEmail() throws Exception {
        String host = "host";
        int port = 1;
        String from = "nobody@nowhere.com";
        String customer = "customer";
        User user = mock(User.class);
        String responseBaseUrl = "responseUrl";
        when(user.getEmail()).thenReturn("user@example.com");

        when(configuration.getHost()).thenReturn(host);
        when(configuration.getPort()).thenReturn(port);
        when(configuration.getFrom()).thenReturn(from);
        when(opportunity.getCustomer()).thenReturn(customer);
        when(renderer.renderEmail(any(View.class)))
                .thenThrow(new IOException("expected-error"));
        EmailComposer composer = new EmailComposer(configuration, renderer);

        composer.composePasswordResetEmail(user, responseBaseUrl);
    }

    @Test(expected = EmailException.class)
    public void FailToAddRecipientToPasswordResetEmail() throws Exception {
        String host = "host";
        int port = 1;
        String from = "nobody@nowhere.com";
        String customer = "customer";
        User user = mock(User.class);
        String responseBaseUrl = "responseUrl";
        when(user.getEmail()).thenReturn("invalid email!!");

        when(configuration.getHost()).thenReturn(host);
        when(configuration.getPort()).thenReturn(port);
        when(configuration.getFrom()).thenReturn(from);
        when(opportunity.getCustomer()).thenReturn(customer);
        EmailComposer composer = new EmailComposer(configuration, renderer);

        composer.composePasswordResetEmail(user, responseBaseUrl);
    }
}