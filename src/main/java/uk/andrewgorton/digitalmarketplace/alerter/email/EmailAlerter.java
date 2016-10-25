package uk.andrewgorton.digitalmarketplace.alerter.email;

import org.apache.commons.mail.EmailException;
import org.apache.commons.mail.HtmlEmail;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import uk.andrewgorton.digitalmarketplace.alerter.Alert;
import uk.andrewgorton.digitalmarketplace.alerter.Opportunity;

import java.util.List;

public class EmailAlerter {
    private static final Logger LOGGER = LoggerFactory.getLogger(EmailAlerter.class);

    private final EmailConfiguration configuration;
    private final EmailComposer composer;

    public EmailAlerter(EmailConfiguration configuration, EmailComposer composer) {
        this.configuration = configuration;
        this.composer = composer;
    }

    public boolean alert(Opportunity o, List<Alert> alerts) {
        boolean result = false;

        try {
            HtmlEmail email = composer.composeAlert(o);
            alerts.stream()
                    .map(Alert::getEmail)
                    // Filter out duplicate email addresses
                    .distinct()
                    .forEach(singleEmail -> {
                        try {
                            email.addTo(singleEmail);
                        } catch (EmailException e) {
                            LOGGER.error(String.format(
                                    "Failed to add address %s to the outbound email", singleEmail), e);
                        }
                    });

            // Send it
            if (configuration.isEnabled()) {
                LOGGER.info("Sending email alert {}", email.getSubject());
                email.send();
            }
            result = true;
        } catch (EmailException e) {
            LOGGER.error("Failed to create email", e);
        }
        return result;
    }
}
