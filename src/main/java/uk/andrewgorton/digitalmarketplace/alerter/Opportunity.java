package uk.andrewgorton.digitalmarketplace.alerter;

import java.sql.Timestamp;

public class Opportunity {
    private long id;
    private String title;
    private String opportunityType;
    private String location;
    private String customer;
    private Timestamp published;
    private Timestamp closing;
    private boolean closed;
    private String excerpt;
    private String url;
    private Timestamp firstSeen;
    private Timestamp lastUpdated;
    private boolean alerted;
    private int duration;
    private long cost;
    private String OMT;
    private boolean removed;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getOpportunityType() {
        return opportunityType;
    }

    public void setOpportunityType(String opportunityType) {
        this.opportunityType = opportunityType;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getCustomer() {
        return customer;
    }

    public void setCustomer(String customer) {
        this.customer = customer;
    }

    public Timestamp getPublished() {
        return published;
    }

    public void setPublished(Timestamp published) {
        this.published = published;
    }

    public Timestamp getClosing() {
        return closing;
    }

    public void setClosing(Timestamp closing) {
        this.closing = closing;
    }

    public boolean isClosed() {
        return closed;
    }

    public void setClosed(boolean closed) {
        this.closed = closed;
    }

    public String getExcerpt() {
        return excerpt;
    }

    public void setExcerpt(String excerpt) {
        this.excerpt = excerpt;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Timestamp getFirstSeen() {
        return firstSeen;
    }

    public void setFirstSeen(Timestamp firstSeen) {
        this.firstSeen = firstSeen;
    }

    public Timestamp getLastUpdated() {
        return lastUpdated;
    }

    public void setLastUpdated(Timestamp lastUpdated) {
        this.lastUpdated = lastUpdated;
    }

    public boolean isAlerted() {
        return alerted;
    }

    public void setAlerted(boolean alerted) {
        this.alerted = alerted;
    }


    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public long getCost() {
        return cost;
    }

    public void setCost(long cost) {
        this.cost = cost;
    }

    public String getOMT() {
        return OMT;
    }

    public void setOMT(String OMT) {
        this.OMT = OMT;
    }

    public boolean isRemoved() {
        return removed;
    }

    public void setRemoved(boolean removed) {
        this.removed = removed;
    }
}
