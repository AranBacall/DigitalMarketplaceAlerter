package uk.andrewgorton.digitalmarketplace.alerter.polling;

import org.eclipse.jetty.http.HttpStatus;
import org.jsoup.Jsoup;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import uk.andrewgorton.digitalmarketplace.alerter.Opportunity;
import uk.andrewgorton.digitalmarketplace.alerter.dao.OpportunityDAO;

import java.util.List;

/**
 * Polls the Digital Marketplace for removed opportunities
 */
public class RemovedOpportunityPoller implements Runnable {
    private static final Logger LOGGER = LoggerFactory.getLogger(RemovedOpportunityPoller.class);
    private final OpportunityDAO opportunityDAO;


    public RemovedOpportunityPoller(OpportunityDAO opportunityDAO) {
        this.opportunityDAO = opportunityDAO;
    }

    @Override
    public void run() {
        try {
            List<Opportunity> localOpportunities = opportunityDAO.findAllUnremoved();
            LOGGER.debug(String.format("Retrieved '%d' active opportunities from db", localOpportunities.size()));

            localOpportunities.forEach(opp -> {
                try {
                    int responseCode = Jsoup.connect(opp.getUrl()).execute().statusCode();
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
