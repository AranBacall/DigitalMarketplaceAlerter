package uk.andrewgorton.digitalmarketplace.alerter.polling;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import uk.andrewgorton.digitalmarketplace.alerter.Opportunity;
import uk.andrewgorton.digitalmarketplace.alerter.OpportunityFactory;
import uk.andrewgorton.digitalmarketplace.alerter.dao.OpportunityDAO;

import java.util.List;

/**
 * Polls the Digital Marketplace for removed opportunities
 */
public class RemovedOpportunityPoller implements Runnable {
    private static final Logger LOGGER = LoggerFactory.getLogger(RemovedOpportunityPoller.class);

    private final Fetcher fetcher;
    private final OpportunityFactory opportunityFactory;
    private final OpportunityDAO opportunityDAO;


    public RemovedOpportunityPoller(Fetcher fetcher, OpportunityFactory opportunityFactory, OpportunityDAO opportunityDAO) {
        this.fetcher = fetcher;
        this.opportunityFactory = opportunityFactory;
        this.opportunityDAO = opportunityDAO;
    }

    @Override
    public void run() {
        try {
            List<Opportunity> liveOpportunities = opportunityFactory.create(fetcher.run());
            LOGGER.info("Fetched {} opportunities from the Digital Marketplace", liveOpportunities.size());
            List<Opportunity> localOpportunities = opportunityDAO.findAllUnremoved();
            LOGGER.info("Fetched {} opportunities from the local store", localOpportunities.size());
            localOpportunities.stream().filter(opp -> !liveOpportunities.contains(opp)).forEach(opp -> {
                opportunityDAO.setRemoved(true, opp.getId());
                LOGGER.info("The following opportunity has been removed from the Digital Marketplace: {}",
                        opp.getUrl());
            });
        } catch (Throwable e) {
            LOGGER.error(e.getMessage(), e);
        }
    }
}
