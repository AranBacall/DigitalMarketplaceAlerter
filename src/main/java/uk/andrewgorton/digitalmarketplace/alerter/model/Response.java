package uk.andrewgorton.digitalmarketplace.alerter.model;

public class Response {

    private long id;
    private String response;
    private String reason;

    public Response(long id, String response, String reason) {
        this.id = id;
        this.response = response;
        this.reason = reason;
    }

    public long getId() {
        return id;
    }

    public String getResponse() {
        return response;
    }

    public String getReason() {
        return reason;
    }
}
