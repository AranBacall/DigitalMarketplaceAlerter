package uk.andrewgorton.digitalmarketplace.alerter.model;

public class BidManager {
    private final long id;
    private final String email;

    public BidManager(long id, String email) {
        this.id = id;
        this.email = email;
    }

    public long getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }
}
