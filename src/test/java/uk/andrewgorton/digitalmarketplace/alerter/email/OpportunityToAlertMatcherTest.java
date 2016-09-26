package uk.andrewgorton.digitalmarketplace.alerter.email;

import org.junit.Before;
import org.junit.Test;
import uk.andrewgorton.digitalmarketplace.alerter.Alert;
import uk.andrewgorton.digitalmarketplace.alerter.Opportunity;
import uk.andrewgorton.digitalmarketplace.alerter.dao.AlertDAO;
import uk.andrewgorton.digitalmarketplace.alerter.dao.OpportunityDAO;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.*;

public class OpportunityToAlertMatcherTest {
    private OpportunityDAO mockOpportunityDAO;
    private AlertDAO mockAlertDAO;
    private EmailAlerter mockEmailAlerter;

    @Before
    public void setup() {
        mockOpportunityDAO = mock(OpportunityDAO.class);
        mockAlertDAO = mock(AlertDAO.class);
        mockEmailAlerter = mock(EmailAlerter.class);
    }

    @Test
    public void NoOpenOpportunitiesDoesNothing() {
        when(mockOpportunityDAO.findAllOpenAndUnalerted()).thenReturn(new ArrayList<>());

        OpportunityToAlertMatcher testObj = new OpportunityToAlertMatcher(mockOpportunityDAO,
                mockAlertDAO,
                mockEmailAlerter);
        testObj.process();

        verify(mockEmailAlerter, never()).alert(any(), any());
    }

    @Test
    public void NoEnabledAlertsDoesNothing() {
        when(mockOpportunityDAO.findAllOpenAndUnalerted()).thenReturn(new ArrayList<>());
        when(mockAlertDAO.findAllEnabled()).thenReturn(new ArrayList<>());

        OpportunityToAlertMatcher testObj = new OpportunityToAlertMatcher(mockOpportunityDAO,
                mockAlertDAO,
                mockEmailAlerter);
        testObj.process();

        verify(mockEmailAlerter, never()).alert(any(), any());
    }

    @Test
    public void NoMatchingAlertDoesNothing() {
        Opportunity o = new Opportunity();
        o.setCustomer("department");
        o.setClosed(false);
        List<Opportunity> opportunities = new ArrayList<>();
        opportunities.add(o);

        Alert a = new Alert(1L, "nobody@example.com", "nonmatching_regex_here", true);
        List<Alert> alertList = new ArrayList<>();
        alertList.add(a);

        when(mockOpportunityDAO.findAllOpenAndUnalerted()).thenReturn(opportunities);
        when(mockAlertDAO.findAllEnabled()).thenReturn(alertList);

        OpportunityToAlertMatcher testObj = new OpportunityToAlertMatcher(mockOpportunityDAO,
                mockAlertDAO,
                mockEmailAlerter);
        testObj.process();

        verify(mockEmailAlerter, never()).alert(any(), any());
    }

    @Test
    public void MatchingAlertCallsEmailAlerter() {
        Opportunity o = new Opportunity();
        o.setCustomer("department");
        o.setClosed(false);
        List<Opportunity> opportunities = new ArrayList<>();
        opportunities.add(o);

        Alert a = new Alert(1L, "nobody@example.com", ".*", true);
        List<Alert> alertList = new ArrayList<>();
        alertList.add(a);

        when(mockOpportunityDAO.findAllOpenAndUnalerted()).thenReturn(opportunities);
        when(mockAlertDAO.findAllEnabled()).thenReturn(alertList);

        OpportunityToAlertMatcher testObj = new OpportunityToAlertMatcher(mockOpportunityDAO,
                mockAlertDAO,
                mockEmailAlerter);
        testObj.process();

        verify(mockEmailAlerter, times(1)).alert(any(), any());
    }
}
