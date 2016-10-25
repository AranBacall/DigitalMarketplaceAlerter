package uk.andrewgorton.digitalmarketplace.alerter.email;

import io.dropwizard.views.View;
import org.apache.commons.mail.EmailException;
import org.apache.commons.mail.HtmlEmail;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import uk.andrewgorton.digitalmarketplace.alerter.Opportunity;
import uk.andrewgorton.digitalmarketplace.alerter.views.email.EmailViewRenderer;

import java.io.IOException;

import static org.junit.Assert.*;
import static org.mockito.Matchers.any;
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
    public void Success() throws Exception {
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

}