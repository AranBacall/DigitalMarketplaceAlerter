package uk.andrewgorton.digitalmarketplace.alerter.polling;


import org.eclipse.jetty.http.HttpStatus;
import org.joda.time.DateTime;
import org.jsoup.Jsoup;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import uk.andrewgorton.digitalmarketplace.alerter.Opportunity;
import uk.andrewgorton.digitalmarketplace.alerter.OpportunityFactory;
import uk.andrewgorton.digitalmarketplace.alerter.dao.OpportunityDAO;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

public class DigitalMarketplacePoller implements Runnable {
    private final Logger LOGGER = LoggerFactory.getLogger(DigitalMarketplacePoller.class);

    private final Fetcher fetcher;
    private final OpportunityFactory opportunityFactory;
    private final OpportunityDAO opportunityDAO;
    private final int notUpdatedThresholdMinutes;

    public DigitalMarketplacePoller(Fetcher fetcher,
                                    OpportunityFactory opportunityFactory,
                                    OpportunityDAO opportunityDAO,
                                    int notUpdatedThresholdMinutes) {
        this.fetcher = fetcher;
        this.opportunityFactory = opportunityFactory;
        this.opportunityDAO = opportunityDAO;
        this.notUpdatedThresholdMinutes = notUpdatedThresholdMinutes;
    }

    @Override
    public void run() {
        fetchOpportunities();
        markRemovedOpportunities();
    }

    private void fetchOpportunities() {
        try {
            List<Opportunity> opportunities = opportunityFactory.create(fetcher.run());
            final List<Opportunity> newOpportunities = new ArrayList<>();
            final List<Opportunity> existingOpen = new ArrayList<>();
            final List<Opportunity> existingClosed = new ArrayList<>();
            opportunities.forEach(x -> {
                if (opportunityDAO.getCountByUrl(x.getUrl()) < 1) {
                    newOpportunities.add(x);
                } else {
                    if (x.isClosed()) {
                        existingClosed.add(x);
                    } else {
                        existingOpen.add(x);
                    }
                }
            });

            opportunityDAO.insert(newOpportunities);
            opportunityDAO.updateOpen(existingOpen);
            opportunityDAO.updateClosed(existingClosed);
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
        }
    }

    private void markRemovedOpportunities() {
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
