package uk.andrewgorton.digitalmarketplace.alerter.email;

import org.apache.commons.mail.HtmlEmail;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import uk.andrewgorton.digitalmarketplace.alerter.Alert;
import uk.andrewgorton.digitalmarketplace.alerter.Opportunity;
import uk.andrewgorton.digitalmarketplace.alerter.views.email.EmailViewRenderer;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.fail;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class EmailAlerterTest {

    private Opportunity openButUnalertedOpportunity;
    private List<Alert> singleAlertMatchAll;
    @Mock
    private EmailComposer emailComposer;
    @Mock
    private EmailViewRenderer viewRenderer;
    @Mock
    private EmailConfiguration configuration;
    @Mock
    private HtmlEmail mockHtmlEmail;

    @Before
    public void Setup() throws Exception {
        Timestamp now = new Timestamp(System.currentTimeMillis());
        openButUnalertedOpportunity = new Opportunity();
        openButUnalertedOpportunity.setId(1L);
        openButUnalertedOpportunity.setTitle("title");
        openButUnalertedOpportunity.setOpportunityType("opportunityType");
        openButUnalertedOpportunity.setLocation("location");
        openButUnalertedOpportunity.setCustomer("customer");
        openButUnalertedOpportunity.setPublished(now);
        openButUnalertedOpportunity.setClosing(now);
        openButUnalertedOpportunity.setClosed(false);
        openButUnalertedOpportunity.setExcerpt("excerpt");
        openButUnalertedOpportunity.setUrl("url");
        openButUnalertedOpportunity.setFirstSeen(now);
        openButUnalertedOpportunity.setLastUpdated(now);
        openButUnalertedOpportunity.setAlerted(false);

        Alert a = new Alert(1, "nobody@nowhere.com", ".*", true);
        singleAlertMatchAll = new ArrayList<>();
        singleAlertMatchAll.add(a);

        when(emailComposer.composeAlert(openButUnalertedOpportunity)).thenReturn(mockHtmlEmail);
    }

    @Test
    public void NoEmailsSentIfDisabled() {
        try {
            when(configuration.isEnabled()).thenReturn(false);
            EmailAlerter testObj = new EmailAlerter(configuration, emailComposer);
            testObj.alert(openButUnalertedOpportunity, singleAlertMatchAll);

            verify(mockHtmlEmail, never()).send();
        } catch (Exception e) {
            fail("Unexpected exception: " + e.getMessage());
        }
    }

    @Test
    public void SingleEmailSentIfEnabledAndMatching() {
        try {
            when(configuration.isEnabled()).thenReturn(true);
            EmailAlerter testObj = new EmailAlerter(configuration, emailComposer);
            testObj.alert(openButUnalertedOpportunity, singleAlertMatchAll);

            verify(mockHtmlEmail, times(1)).send();
        } catch (Exception e) {
            fail("Unexpected exception: " + e.getMessage());
        }
    }

    @Test
    public void RecipientAddedOnlyOnce() {
        when(configuration.isEnabled()).thenReturn(true);
        List<Alert> alertList = new ArrayList<>();
        Alert a = new Alert(1L, "nobody@example.com", ".*", true);
        alertList.add(a);
        a = new Alert(2L, "nobody@example.com", ".*", true);
        alertList.add(a);

        try {
            EmailAlerter testObj = new EmailAlerter(configuration, emailComposer);
            testObj.alert(openButUnalertedOpportunity, alertList);

            verify(mockHtmlEmail, times(1)).addTo(a.getEmail());
        } catch (Exception e) {
            fail("Unexpected exception: " + e.getMessage());
        }
    }

    @Test
    public void InvalidEmailProvided() {
        when(configuration.isEnabled()).thenReturn(true);
        List<Alert> alertList = new ArrayList<>();
        Alert a = new Alert(1L, "1234", ".*", true);
        alertList.add(a);
        a = new Alert(2L, "nobody@example.com", ".*", true);
        alertList.add(a);

        try {
            EmailAlerter testObj = new EmailAlerter(configuration, emailComposer);
            testObj.alert(openButUnalertedOpportunity, alertList);

            verify(mockHtmlEmail, times(1)).addTo(a.getEmail());
        } catch (Exception e) {
            fail("Unexpected exception: " + e.getMessage());
        }
    }
}
