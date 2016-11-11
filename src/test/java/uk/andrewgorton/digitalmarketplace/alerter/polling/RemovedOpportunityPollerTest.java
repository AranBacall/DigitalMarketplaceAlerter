package uk.andrewgorton.digitalmarketplace.alerter.polling;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import uk.andrewgorton.digitalmarketplace.alerter.Opportunity;
import uk.andrewgorton.digitalmarketplace.alerter.OpportunityFactory;
import uk.andrewgorton.digitalmarketplace.alerter.dao.OpportunityDAO;

import java.util.Arrays;
import java.util.List;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyLong;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.*;

/**
 * Tests for {@link RemovedOpportunityPoller}
 */
@RunWith(MockitoJUnitRunner.class)
public class RemovedOpportunityPollerTest {

    @Mock
    private Fetcher fetcher;
    @Mock
    private OpportunityFactory factory;
    @Mock
    private OpportunityDAO dao;

    @Test
    public void ProperlyMarksRemovedOpportunities() throws Exception {
        Opportunity a = new Opportunity();
        a.setId(1234);
        a.setUrl("https://digital.marketplace.com/opportunity/A");
        Opportunity b = new Opportunity();
        b.setId(5678);
        b.setUrl("https://digital.marketplace.com/opportunity/B");
        Opportunity c = new Opportunity();
        Opportunity d = new Opportunity();

        when(factory.create(any())).thenReturn(Arrays.asList(c, d));
        when(dao.findAllUnremoved()).thenReturn(Arrays.asList(a, b, c, d));
        RemovedOpportunityPoller poller = new RemovedOpportunityPoller(fetcher, factory, dao);

        poller.run();

        verify(dao).setRemoved(true, a.getId());
        verify(dao).setRemoved(true, b.getId());
    }

    @Test
    public void NoneToMarkRemoved() throws Exception {
        Opportunity a = new Opportunity();
        Opportunity b = new Opportunity();
        Opportunity c = new Opportunity();
        Opportunity d = new Opportunity();

        List opportunities = Arrays.asList(a, b, c, d);
        when(factory.create(any())).thenReturn(opportunities);
        when(dao.findAllUnremoved()).thenReturn(opportunities);
        RemovedOpportunityPoller poller = new RemovedOpportunityPoller(fetcher, factory, dao);

        poller.run();

        verify(dao, never()).setRemoved(eq(true), anyLong());
    }

}