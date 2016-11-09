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
        List<Opportunity> opportunities = opportunityFactory.create(fetcher.run());
        List<Opportunity> allOpen = opportunityDAO.findAllOpen();
        allOpen.stream().filter(opp -> !opportunities.contains(opp)).forEach(opp -> {
            opportunityDAO.setRemoved(true, opp.getId());
            LOGGER.info("The following opportunity has been removed from the Digital Marketplace: {}", opp.getUrl());
        });
    }
}
