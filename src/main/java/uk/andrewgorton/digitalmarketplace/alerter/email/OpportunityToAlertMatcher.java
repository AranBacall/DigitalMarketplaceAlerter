package uk.andrewgorton.digitalmarketplace.alerter.email;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import uk.andrewgorton.digitalmarketplace.alerter.Alert;
import uk.andrewgorton.digitalmarketplace.alerter.ExceptionFormatter;
import uk.andrewgorton.digitalmarketplace.alerter.Opportunity;
import uk.andrewgorton.digitalmarketplace.alerter.dao.AlertDAO;
import uk.andrewgorton.digitalmarketplace.alerter.dao.OpportunityDAO;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

public class OpportunityToAlertMatcher {
    private final Logger LOGGER = LoggerFactory.getLogger(EmailAlertRunner.class);
    private final OpportunityDAO opportunityDAO;
    private final AlertDAO alertDAO;
    private final EmailAlerter emailAlerter;

    public OpportunityToAlertMatcher(OpportunityDAO opportunityDAO, AlertDAO alertDAO, EmailAlerter emailAlerter) {
        this.opportunityDAO = opportunityDAO;
        this.alertDAO = alertDAO;
        this.emailAlerter = emailAlerter;
    }

    public void process() {
        try {
            List<Opportunity> unalertedOpportunities = opportunityDAO.findAllOpenAndUnalerted();
            List<Alert> alerts = alertDAO.findAllEnabled();

            // Map people to opportunities
            Map<Opportunity, List<Alert>> whoToAlert = new HashMap<>();
            alerts.forEach(singleAlert -> {
                Pattern p = Pattern.compile(singleAlert.getCustomerMatchRegex(), Pattern.CASE_INSENSITIVE);
                unalertedOpportunities.forEach(singleOpportunity -> {
                    // Alert only on open opportunities...
                    if (!singleOpportunity.isClosed()) {
                        if (p.matcher(singleOpportunity.getCustomer()).find()) {
                            // Pattern matches
                            if (!whoToAlert.containsKey(singleOpportunity)) {
                                whoToAlert.put(singleOpportunity, new ArrayList<>());
                            }
                            // Ensure we only add people once
                            if (!whoToAlert.get(singleOpportunity).contains(singleAlert)) {
                                whoToAlert.get(singleOpportunity).add(singleAlert);
                            }
                        }
                    }
                });
            });

            // Now alert the users
            whoToAlert.entrySet().forEach(opportunityToAlert -> {
                if (emailAlerter.alert(opportunityToAlert.getKey(), opportunityToAlert.getValue())) {
                    opportunityDAO.markAlerted(opportunityToAlert.getKey().getId());
                }
            });
        } catch (Exception e) {
            LOGGER.error(e.getMessage());
            LOGGER.debug(ExceptionFormatter.formatAsString(e));
        }
    }
}
