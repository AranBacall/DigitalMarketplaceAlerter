package uk.andrewgorton.digitalmarketplace.alerter.polling;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Fetcher {
    private final Logger LOGGER = LoggerFactory.getLogger(Fetcher.class);

    private final String website = "https://www.digitalmarketplace.service.gov.uk/digital-outcomes-and-specialists/opportunities";

    public Fetcher() {

    }

    public Document run() {
        try {
            return Jsoup.connect(website).get();
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
        }
        return null;
    }
}