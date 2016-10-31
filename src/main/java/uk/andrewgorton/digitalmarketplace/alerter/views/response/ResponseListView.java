package uk.andrewgorton.digitalmarketplace.alerter.views.response;

import io.dropwizard.views.View;
import uk.andrewgorton.digitalmarketplace.alerter.model.Response;

import java.util.List;

/**
 * Represents an HTML page that a bid manager will visit to respond to an email
 */
public class ResponseListView extends View {

    private Long opportunity;
    private List<Response> responses;

    public ResponseListView(Long opportunity, List<Response> responses) {
        super("listview.ftl");
        this.opportunity = opportunity;
        this.responses = responses;
    }

    public Long getOpportunity() {
        return opportunity;
    }

    public List<Response> getResponses() {
        return responses;
    }
}
