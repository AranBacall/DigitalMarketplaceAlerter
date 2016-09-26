package uk.andrewgorton.digitalmarketplace.alerter;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

public class OpportunityFactory {
    private static final DateTimeFormatter dateTimeFormatter = DateTimeFormat.forPattern("EEEE dd MMM yyyy");

    public List<Opportunity> create(Document d) {
        List<Opportunity> opportunities = new ArrayList<>();
        Elements searchResults = d.select("div.search-result");
        searchResults.forEach(singleResult -> {
            opportunities.add(create(singleResult));
        });
        return opportunities;
    }

    private Opportunity create(Element singleResult) {
        Elements titleAndUrl = singleResult.select("h2.search-result-title");
        String url = titleAndUrl.select("a[href]").attr("abs:href");
        String title = titleAndUrl.select("a[href]").html();

        Elements departmentAndLocation = singleResult.select("ul.search-result-important-metadata").select("li.search-result-metadata-item");
        String department = departmentAndLocation.get(0).html();
        String location = departmentAndLocation.get(1).html();

        Elements meta = singleResult.select("ul.search-result-metadata");
        String type = meta.get(0).select("li.search-result-metadata-item").html();
        Elements publishedAndClosing = meta.get(1).select("li.search-result-metadata-item");
        boolean closed = false;
        DateTime published = null;
        DateTime closing = null;
        if (publishedAndClosing.size() == 1) {
            closed = true;
        } else {
            published = dateTimeFormatter.parseDateTime(publishedAndClosing.get(0).html().replaceFirst("^Published: ", ""));
            closing = dateTimeFormatter.parseDateTime(publishedAndClosing.get(1).html().replaceFirst("^Closing: ", ""));
        }

        String excerpt = singleResult.select("p.search-result-excerpt").html();

        DateTime fakeClosing = new DateTime().minusYears(100);

        Opportunity tempOpportunity = new Opportunity();
        tempOpportunity.setUrl(url);
        tempOpportunity.setTitle(title);
        tempOpportunity.setCustomer(department);
        tempOpportunity.setLocation(location);
        tempOpportunity.setOpportunityType(type);
        tempOpportunity.setClosed(closed);
        if (closed) {
            tempOpportunity.setPublished(new Timestamp(fakeClosing.getMillis()));
            tempOpportunity.setClosing(new Timestamp(fakeClosing.getMillis()));
        } else {
            tempOpportunity.setPublished(new Timestamp(published.getMillis()));
            tempOpportunity.setClosing(new Timestamp(closing.getMillis()));
        }
        tempOpportunity.setExcerpt(excerpt);
        tempOpportunity.setLastUpdated(new Timestamp(System.currentTimeMillis()));
        return tempOpportunity;
    }
}
