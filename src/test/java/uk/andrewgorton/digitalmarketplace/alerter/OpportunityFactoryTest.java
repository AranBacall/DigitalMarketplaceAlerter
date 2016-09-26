package uk.andrewgorton.digitalmarketplace.alerter;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.junit.Assert;
import org.junit.Test;

import java.io.File;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class OpportunityFactoryTest {

    @Test
    public void OpportunitiesCreatedFromHtml() {
        try {
            File input = new File(getClass().getClassLoader().getResource("digitalmarketplace_curled.html").getFile());
            Document doc = Jsoup.parse(input, "UTF-8", "http://localhost/");

            OpportunityFactory testObj = new OpportunityFactory();
            List<Opportunity> opportunities = testObj.create(doc);
            assertEquals("There should be 100 opportunities created", 100, opportunities.size());
        } catch (Exception e) {
            Assert.fail("Exception: " + e.getMessage());
        }
    }
}
