package uk.andrewgorton.digitalmarketplace.alerter.resources;

import com.codahale.metrics.annotation.Timed;
import uk.andrewgorton.digitalmarketplace.alerter.annotations.LoginRequired;
import uk.andrewgorton.digitalmarketplace.alerter.dao.report.ReportingDAO;
import uk.andrewgorton.digitalmarketplace.alerter.report.KeyValueColorItem;
import uk.andrewgorton.digitalmarketplace.alerter.views.report.ReportView;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;
import java.util.regex.Pattern;

@Path("/report")
public class ReportResource {
    private class StringPatternPair {
        private String key;
        private Pattern value;

        public StringPatternPair(String key, Pattern value) {
            this.key = key;
            this.value = value;
        }

        public String getKey() {
            return key;
        }

        public Pattern getValue() {
            return value;
        }
    }

    private final ReportingDAO reportingDAO;
    private final List<StringPatternPair> patterns = new ArrayList<>();

    public ReportResource(ReportingDAO reportingDAO) {
        this.reportingDAO = reportingDAO;

        patterns.add(new StringPatternPair("National Offender Management Service", Pattern.compile("NOMS", Pattern.CASE_INSENSITIVE)));
        patterns.add(new StringPatternPair("National Offender Management Service", Pattern.compile("National Offender Management Service", Pattern.CASE_INSENSITIVE)));

        patterns.add(new StringPatternPair("Home Office", Pattern.compile("Home Office", Pattern.CASE_INSENSITIVE)));

        patterns.add(new StringPatternPair("Government Digital Service", Pattern.compile("GDS", Pattern.CASE_INSENSITIVE)));
        patterns.add(new StringPatternPair("Government Digital Service", Pattern.compile("Government Digital Service", Pattern.CASE_INSENSITIVE)));

        patterns.add(new StringPatternPair("Her Majesty's Passport Office", Pattern.compile("HMPO", Pattern.CASE_INSENSITIVE)));
        patterns.add(new StringPatternPair("Her Majesty's Passport Office", Pattern.compile("Passport Office", Pattern.CASE_INSENSITIVE)));

        patterns.add(new StringPatternPair("Ministry of Defence", Pattern.compile("Ministry of Defence", Pattern.CASE_INSENSITIVE)));
        patterns.add(new StringPatternPair("Ministry of Defence", Pattern.compile("\\bMOD\\b", Pattern.CASE_INSENSITIVE)));

        patterns.add(new StringPatternPair("Environment Agency", Pattern.compile("Environment Agency", Pattern.CASE_INSENSITIVE)));

        patterns.add(new StringPatternPair("Department for Environment, Food and Rural Affairs", Pattern.compile("Environment, Food and Rural Affairs", Pattern.CASE_INSENSITIVE)));
        patterns.add(new StringPatternPair("Department for Environment, Food and Rural Affairs", Pattern.compile("Environment, Food & Rural Affairs", Pattern.CASE_INSENSITIVE)));

        patterns.add(new StringPatternPair("Office for Standards in Education, Children's Services and Skills", Pattern.compile("OFSTED", Pattern.CASE_INSENSITIVE)));
        patterns.add(new StringPatternPair("Office for Standards in Education, Children's Services and Skills", Pattern.compile("Standards in Education", Pattern.CASE_INSENSITIVE)));

        patterns.add(new StringPatternPair("Foreign and Commonwealth Office", Pattern.compile("\\bFCO\\b", Pattern.CASE_INSENSITIVE)));
        patterns.add(new StringPatternPair("Foreign and Commonwealth Office", Pattern.compile("Foreign and Commonwealth Office", Pattern.CASE_INSENSITIVE)));

        patterns.add(new StringPatternPair("Ministry of Justice", Pattern.compile("\\bMOJ\\b", Pattern.CASE_INSENSITIVE)));
        patterns.add(new StringPatternPair("Ministry of Justice", Pattern.compile("Ministry of Justice", Pattern.CASE_INSENSITIVE)));
    }

    @GET
    @Timed
    @LoginRequired
    public Object getReportView(@Context HttpServletRequest request) {
        return new ReportView();
    }

    @GET
    @Path("/opc")
    @Timed
    @LoginRequired
    @Produces("application/json")
    public Object getOpportunitiesPerCustomer(@Context HttpServletRequest request) {
        List<KeyValueColorItem> allItems = aggregateOpportunities(consolidateCustomers(reportingDAO.findOpportunitiesPerCustomer()));
        allItems.sort(new Comparator<KeyValueColorItem>() {
            @Override
            public int compare(KeyValueColorItem o1, KeyValueColorItem o2) {
                if (o1.getValue() < o2.getValue()) {
                    return 1;
                } else if (o1.getValue() > o2.getValue()) {
                    return -1;
                }
                return 0;
            }
        });
        return allItems;
    }

    private List<KeyValueColorItem> aggregateOpportunities(List<KeyValueColorItem> allItems) {
        // Total number of opportunities
        AtomicLong total = new AtomicLong();
        allItems.forEach(e -> total.addAndGet(e.getValue()));

        // Find the number of opportunities which breach the 25% threshold
        AtomicLong thresholdNumber = new AtomicLong();
        AtomicLong aggregatedOpportunities = new AtomicLong();
        while (((float) aggregatedOpportunities.get() / (float) total.get()) < 0.15) {
            AtomicLong currentThresholdOpportunityCount = new AtomicLong();
            allItems.stream()
                    .filter(e -> e.getValue() == thresholdNumber.get())
                    .forEach(e -> currentThresholdOpportunityCount.addAndGet(e.getValue()));

            allItems.removeIf(e -> e.getValue() == thresholdNumber.get());

            aggregatedOpportunities.addAndGet(currentThresholdOpportunityCount.get());
            thresholdNumber.incrementAndGet();
        }
        allItems.add(new KeyValueColorItem("Other", aggregatedOpportunities.get()));
        return allItems;
    }

    private List<KeyValueColorItem> consolidateCustomers(List<KeyValueColorItem> allItems) {
        HashMap<String, AtomicLong> consolidatedCustomers = new HashMap<>();
        List<KeyValueColorItem> consolidated = new ArrayList<>();
        allItems.forEach(singleItem -> {
            boolean patternFound = false;

            for (StringPatternPair singlePattern : patterns) {
                if (singlePattern.getValue().matcher(singleItem.getLabel()).find()) {
                    if (!consolidatedCustomers.containsKey(singlePattern.getKey())) {
                        consolidatedCustomers.put(singlePattern.getKey(), new AtomicLong());
                    }
                    consolidatedCustomers.get(singlePattern.getKey()).addAndGet(singleItem.getValue());
                    patternFound = true;
                    break;
                }
            }
            if (!patternFound) {
                consolidatedCustomers.put(singleItem.getLabel(), new AtomicLong(singleItem.getValue()));
            }
        });

        List<KeyValueColorItem> result = new ArrayList<>();
        consolidatedCustomers.entrySet().forEach(singleItem -> {
            result.add(new KeyValueColorItem(singleItem.getKey(), singleItem.getValue().longValue()));
        });

        return result;
    }

    @GET
    @Path("/opl")
    @Timed
    @LoginRequired
    @Produces("application/json")
    public Object getOpportunitiesPerLocation(@Context HttpServletRequest request) {
        return reportingDAO.findOpportunitiesPerLocation();
    }

    @GET
    @Path("/responses")
    @Timed
    @LoginRequired
    @Produces("application/json")
    public Object responses() {
        return reportingDAO.responses();
    }

    @GET
    @Path("/bids-against-opportunities")
    @Timed
    @LoginRequired
    @Produces("application/json")
    public Object bidsAgainstOpportunities() {
        return reportingDAO.bidsAgainstOpportunities();
    }
}
