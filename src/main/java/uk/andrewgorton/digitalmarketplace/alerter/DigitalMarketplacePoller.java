package uk.andrewgorton.digitalmarketplace.alerter;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import uk.andrewgorton.digitalmarketplace.alerter.dao.OpportunityDAO;

import java.util.ArrayList;
import java.util.List;

public class DigitalMarketplacePoller implements Runnable {
    private final Logger LOGGER = LoggerFactory.getLogger(DigitalMarketplacePoller.class);

    private final Fetcher fetcher;
    private final OpportunityFactory opportunityFactory;
    private final OpportunityDAO opportunityDAO;

    public DigitalMarketplacePoller(Fetcher fetcher,
                                    OpportunityFactory opportunityFactory,
                                    OpportunityDAO opportunityDAO) {
        this.fetcher = fetcher;
        this.opportunityFactory = opportunityFactory;
        this.opportunityDAO = opportunityDAO;
    }

    @Override
    public void run() {
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
}
