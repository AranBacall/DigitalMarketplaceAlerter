package uk.andrewgorton.digitalmarketplace.alerter.email;

import org.apache.commons.mail.EmailException;
import org.apache.commons.mail.HtmlEmail;
import org.apache.commons.validator.routines.EmailValidator;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import uk.andrewgorton.digitalmarketplace.alerter.Opportunity;
import uk.andrewgorton.digitalmarketplace.alerter.User;

import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.*;

/**
 * Created by ross on 26/10/16.
 */
@RunWith(MockitoJUnitRunner.class)
public class EmailServiceTest {

    @Mock
    private EmailConfiguration configuration;
    @Mock
    private EmailComposer composer;
    @Mock
    private EmailValidator validator;
    @Mock
    private Opportunity opportunity;

    private HtmlEmail email;

    @Before
    public void setUp() throws Exception {
        email = spy(new HtmlEmail());
        doReturn("").when(email).send();
    }


    @Test
    public void Success() throws Exception {
        String responseUrl = "responseUrl";
        String[] emails = {"test@example.com", "test2@example.com"};
        when(configuration.isEnabled()).thenReturn(true);
        when(validator.isValid(anyString())).thenReturn(true);
        when(composer.composeBidManagerEmail(opportunity, responseUrl)).thenReturn(email);
        EmailService service = new EmailService(composer, configuration, validator);

        service.sendBidManagerEmail(opportunity, emails, responseUrl);

        for (String e : emails) {
            verify(email).addTo(e);
        }
        verify(email).send();
    }

    @Test(expected = EmailException.class)
    public void EmailConfigNotEnabled() throws Exception {
        String[] emails = {"test@example.com", "test2@example.com"};
        EmailService service = new EmailService(composer, configuration, validator);

        service.sendBidManagerEmail(opportunity, emails, "");
        verify(composer, never()).composeAlert(any(Opportunity.class));
    }

    @Test
    public void EmailsTrimmedAndDeDuplicated() throws Exception {
        String responseUrl = "responseUrl";
        String[] emails = {"  test@example.com", "test@example.com  ", "test2@example.com"};
        when(validator.isValid(anyString())).thenReturn(true);
        when(configuration.isEnabled()).thenReturn(true);
        when(composer.composeBidManagerEmail(opportunity, responseUrl)).thenReturn(email);
        EmailService service = new EmailService(composer, configuration, validator);

        service.sendBidManagerEmail(opportunity, emails, responseUrl);

        verify(email, times(2)).addTo(anyString());
    }

    @Test(expected = EmailException.class)
    public void AllEmailsInvalidOrNoneProvided() throws Exception {
        String[] emails = {"  test@example.com", "test@example.com  ", "test2@example.com"};
        EmailService service = new EmailService(composer, configuration, validator);

        service.sendBidManagerEmail(opportunity, emails, "");

        verify(email, never()).send();
    }

    @Test
    public void SendPasswordResetEmail() throws Exception {
        User user = mock(User.class);
        String responseUrl = "responseUrl";
        when(configuration.isEnabled()).thenReturn(true);
        when(validator.isValid(anyString())).thenReturn(true);
        when(composer.composePasswordResetEmail(user, responseUrl)).thenReturn(email);
        EmailService service = new EmailService(composer, configuration, validator);

        service.sendPasswordResetEmail(user, responseUrl);

        verify(email).send();
    }

    @Test(expected = EmailException.class)
    public void SendPasswordResetEmailEmailConfigNotEnabled() throws Exception {
        EmailService service = new EmailService(composer, configuration, validator);
        service.sendPasswordResetEmail(null, null);
        verify(composer, never()).composePasswordResetEmail(any(User.class), anyString());
    }

}