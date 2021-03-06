package uk.andrewgorton.digitalmarketplace.alerter;

import org.junit.Test;
import uk.andrewgorton.digitalmarketplace.alerter.polling.DigitalMarketplacePoller;
import uk.andrewgorton.digitalmarketplace.alerter.polling.Fetcher;

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
        DigitalMarketplacePoller poller = new DigitalMarketplacePoller(fetcher, null, null, 4);
        poller.run();
    }

}