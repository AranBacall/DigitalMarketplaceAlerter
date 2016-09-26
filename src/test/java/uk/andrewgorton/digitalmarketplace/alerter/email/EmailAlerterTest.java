package uk.andrewgorton.digitalmarketplace.alerter.email;

import org.apache.commons.mail.HtmlEmail;
import org.junit.Before;
import org.junit.Test;
import uk.andrewgorton.digitalmarketplace.alerter.Alert;
import uk.andrewgorton.digitalmarketplace.alerter.Opportunity;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.fail;
import static org.mockito.Mockito.*;

public class EmailAlerterTest {
    private Opportunity openButUnalertedOpportunity;
    private List<Alert> singleAlertMatchAll;
    private HtmlEmail mockHtmlEmail;
    private HtmlEmailBodyFactory mockHtmlEmailBodyFactory;
    private TextEmailBodyFactory mockTextEmailBodyFactory;

    @Before
    public void Setup() {
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

        mockHtmlEmail = mock(HtmlEmail.class);
        mockHtmlEmailBodyFactory = mock(HtmlEmailBodyFactory.class);
        mockTextEmailBodyFactory = mock(TextEmailBodyFactory.class);
    }

    @Test
    public void NoEmailsSentIfDisabled() {
        try {
            EmailAlerter testObj = new EmailAlerter("host",
                    25,
                    "nobody@example.com",
                    mockHtmlEmailBodyFactory,
                    mockTextEmailBodyFactory,
                    false,
                    mockHtmlEmail);
            testObj.alert(openButUnalertedOpportunity, singleAlertMatchAll);

            verify(mockHtmlEmail, never()).send();
        } catch (Exception e) {
            fail("Unexpected exception: " + e.getMessage());
        }
    }

    @Test
    public void SingleEmailSentIfEnabledAndMatching() {
        try {
            EmailAlerter testObj = new EmailAlerter("host",
                    25,
                    "nobody@example.com",
                    mockHtmlEmailBodyFactory,
                    mockTextEmailBodyFactory,
                    true,
                    mockHtmlEmail);
            testObj.alert(openButUnalertedOpportunity, singleAlertMatchAll);

            verify(mockHtmlEmail, times(1)).send();
        } catch (Exception e) {
            fail("Unexpected exception: " + e.getMessage());
        }
    }

    @Test
    public void RecipientAddedOnlyOnce() {
        List<Alert> alertList = new ArrayList<>();
        Alert a = new Alert(1L, "nobody@example.com", ".*", true);
        alertList.add(a);
        a = new Alert(2L, "nobody@example.com", ".*", true);
        alertList.add(a);

        try {
            EmailAlerter testObj = new EmailAlerter("host",
                    25,
                    "nobody@example.com",
                    mockHtmlEmailBodyFactory,
                    mockTextEmailBodyFactory,
                    true,
                    mockHtmlEmail);
            testObj.alert(openButUnalertedOpportunity, alertList);

            verify(mockHtmlEmail, times(1)).addTo(a.getEmail());
        } catch (Exception e) {
            fail("Unexpected exception: " + e.getMessage());
        }
    }

    @Test
    public void EmailHostSetAsSpecified() {
        String testHost = "some.host.here";
        int port = 65535;

        try {
            EmailAlerter testObj = new EmailAlerter(testHost,
                    port,
                    "nobody@example.com",
                    mockHtmlEmailBodyFactory,
                    mockTextEmailBodyFactory,
                    false,
                    mockHtmlEmail);
            testObj.alert(openButUnalertedOpportunity, singleAlertMatchAll);

            verify(mockHtmlEmail, times(1)).setHostName(testHost);
            verify(mockHtmlEmail, times(1)).setSmtpPort(port);
        } catch (Exception e) {
            fail("Unexpected exception: " + e.getMessage());
        }
    }

    @Test
    public void FromAddressSetAsSupplied() {
        String fromAddress = "some_random_address@nowhere.com";

        try {
            EmailAlerter testObj = new EmailAlerter("localhost",
                    25,
                    fromAddress,
                    mockHtmlEmailBodyFactory,
                    mockTextEmailBodyFactory,
                    false,
                    mockHtmlEmail);
            testObj.alert(openButUnalertedOpportunity, singleAlertMatchAll);

            verify(mockHtmlEmail, times(1)).setFrom(fromAddress);
        } catch (Exception e) {
            fail("Unexpected exception: " + e.getMessage());
        }
    }
}
