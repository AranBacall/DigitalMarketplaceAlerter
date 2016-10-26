package uk.andrewgorton.digitalmarketplace.alerter;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.dropwizard.Configuration;
import io.dropwizard.db.DataSourceFactory;
import uk.andrewgorton.digitalmarketplace.alerter.email.EmailConfiguration;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

public class DigitalMarketplaceAlerterConfiguration extends Configuration {
    @Valid
    @NotNull
    private DataSourceFactory database = new DataSourceFactory();

    @Valid
    @NotNull
    private String sessionCookieName;

    @Valid
    @NotNull
    private EmailConfiguration emailConfiguration = new EmailConfiguration();

    public DataSourceFactory getDatabase() {
        return database;
    }

    @JsonProperty("database")
    public void setDatabase(DataSourceFactory database) {
        this.database = database;
    }

    public String getSessionCookieName() {
        return sessionCookieName;
    }

    @JsonProperty("sessionCookieName")
    public void setSessionCookieName(String sessionCookieName) {
        this.sessionCookieName = sessionCookieName;
    }

    public EmailConfiguration getEmailConfiguration() {
        return emailConfiguration;
    }

    @JsonProperty("email")
    public void setEmailConfiguration(EmailConfiguration emailConfiguration) {
        this.emailConfiguration = emailConfiguration;
    }
}
