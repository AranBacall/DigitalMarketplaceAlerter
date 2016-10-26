package uk.andrewgorton.digitalmarketplace.alerter.email;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.apache.commons.mail.EmailException;
import org.apache.commons.mail.HtmlEmail;
import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;

public class EmailConfiguration {
    @NotEmpty
    private String host;

    @Min(1)
    @Max(65535)
    private int port = 25;

    @NotEmpty
    private String from;

    @NotEmpty
    private String adminEmail;

    @NotEmpty
    private String adminName;

    private boolean enabled;

    @JsonProperty
    public String getHost() {
        return host;
    }

    @JsonProperty
    public void setHost(String host) {
        this.host = host;
    }

    @JsonProperty
    public int getPort() {
        return port;
    }

    @JsonProperty
    public void setPort(int port) {
        this.port = port;
    }

    @JsonProperty
    public String getFrom() {
        return from;
    }

    @JsonProperty
    public void setFrom(String from) {
        this.from = from;
    }

    @JsonProperty
    public String getAdminEmail() {
        return adminEmail;
    }

    @JsonProperty
    public void setAdminEmail(String adminEmail) {
        this.adminEmail = adminEmail;
    }

    @JsonProperty
    public String getAdminName() {
        return adminName;
    }

    @JsonProperty
    public void setAdminName(String adminName) {
        this.adminName = adminName;
    }

    @JsonProperty
    public boolean isEnabled() {
        return enabled;
    }

    @JsonProperty
    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

}
