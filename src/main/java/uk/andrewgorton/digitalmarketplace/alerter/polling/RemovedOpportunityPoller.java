package uk.andrewgorton.digitalmarketplace.alerter.polling;

import org.eclipse.jetty.http.HttpStatus;
import org.joda.time.DateTime;
import org.jsoup.Jsoup;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import uk.andrewgorton.digitalmarketplace.alerter.Opportunity;
import uk.andrewgorton.digitalmarketplace.alerter.dao.OpportunityDAO;

import java.sql.Timestamp;
import java.util.List;

/**
 * Polls the Digital Marketplace for removed opportunities
 */
public class RemovedOpportunityPoller implements Runnable {
    private static final Logger LOGGER = LoggerFactory.getLogger(RemovedOpportunityPoller.class);
    private final OpportunityDAO opportunityDAO;
    private final int notUpdatedThresholdMinutes;


    public RemovedOpportunityPoller(OpportunityDAO opportunityDAO, int notUpdatedThresholdMinutes) {
        this.opportunityDAO = opportunityDAO;
        this.notUpdatedThresholdMinutes = notUpdatedThresholdMinutes;
    }

    @Override
    public void run() {
        DateTime notUpdatedThreshold = new DateTime().minusMinutes(notUpdatedThresholdMinutes);

        try {
            List<Opportunity> notUpdatedOpportunities = opportunityDAO.findAllNotUpdatedSince(new Timestamp(notUpdatedThreshold.getMillis()));
            notUpdatedOpportunities.forEach(opp -> {
                try {
                    int responseCode = Jsoup.connect(opp.getUrl())
                            .ignoreHttpErrors(true)
                            .execute()
                            .statusCode();
                    if (responseCode == HttpStatus.NOT_FOUND_404) {
                        opportunityDAO.setRemoved(true, opp.getId());
                        LOGGER.debug(String.format("Opportunity '%s' (%d) marked as removed", opp.getTitle(), opp.getId()));
                    }
                } catch (Exception e) {
                    LOGGER.error(e.getMessage(), e);
                }
            });
        } catch (Throwable e) {
            LOGGER.error(e.getMessage(), e);
        }
    }
}
