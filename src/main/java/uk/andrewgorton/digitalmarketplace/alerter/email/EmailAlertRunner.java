package uk.andrewgorton.digitalmarketplace.alerter.email;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import uk.andrewgorton.digitalmarketplace.alerter.ExceptionFormatter;

public class EmailAlertRunner implements Runnable {
    private final Logger LOGGER = LoggerFactory.getLogger(EmailAlertRunner.class);
    private final OpportunityToAlertMatcher opportunityToAlertMatcher;


    public EmailAlertRunner(OpportunityToAlertMatcher opportunityToAlertMatcher) {
        this.opportunityToAlertMatcher = opportunityToAlertMatcher;
    }

    @Override
    public void run() {
        try {
            opportunityToAlertMatcher.process();
        } catch (Exception e) {
            LOGGER.error(e.getMessage());
            LOGGER.debug(ExceptionFormatter.formatAsString(e));
        }
    }
}
