package uk.andrewgorton.digitalmarketplace.alerter;

import org.junit.Test;

import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;

public class DigitalMarketplacePollerTest {

    /**
     * Manually inspected log results to confirm that the removal
     * of ExceptionFormatter did not affect the display of an exception in the logs.
     */
    @Test
    public void FormattedExceptionDisplaysCorrectly() {
        Fetcher fetcher = mock(Fetcher.class);
        doThrow(new RuntimeException("error message")).when(fetcher).run();
        DigitalMarketplacePoller poller = new DigitalMarketplacePoller(fetcher, null, null);
        poller.run();
    }

}