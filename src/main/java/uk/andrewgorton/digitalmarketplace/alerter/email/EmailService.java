package uk.andrewgorton.digitalmarketplace.alerter.email;

import org.apache.commons.mail.EmailException;
import org.apache.commons.mail.HtmlEmail;
import org.apache.commons.validator.routines.EmailValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import uk.andrewgorton.digitalmarketplace.alerter.Opportunity;

import java.util.Arrays;

/**
 * Created by ross on 26/10/16.
 */
public class EmailService {

    private static final Logger LOGGER = LoggerFactory.getLogger(EmailService.class);

    private final EmailComposer composer;
    private final EmailConfiguration configuration;
    private final EmailValidator validator;

    public EmailService(EmailComposer composer, EmailConfiguration configuration,
                        EmailValidator validator) {
        this.composer = composer;
        this.configuration = configuration;
        this.validator = validator;
    }

    public void sendBidManagerEmail(Opportunity opportunity, String[] emailArray) throws EmailException {

        if (!configuration.isEnabled()) {
            throw new EmailException("Email configuration not enabled");
        }

        HtmlEmail email = composer.composeAlert(opportunity);
        Arrays.stream(emailArray)
                .map(String::trim)
                .distinct()
                //.filter(validator::isValid)
                .forEach(bm -> {
                    try {
                        email.addTo(bm);
                    } catch (EmailException e) {
                        LOGGER.error(String.format(
                                "Failed to add manager %s to the outbound email", bm), e);
                    }
                });

        if (email.getToAddresses().isEmpty()) {
            throw new EmailException("No email addresses present in outbound email, not sending");
        }

        LOGGER.info("Sending email {}", email.getSubject());
        email.send();
    }
}
