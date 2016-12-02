package uk.andrewgorton.digitalmarketplace.alerter.email;

import org.apache.commons.mail.EmailException;
import org.apache.commons.mail.HtmlEmail;
import org.apache.commons.validator.routines.EmailValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import uk.andrewgorton.digitalmarketplace.alerter.Opportunity;
import uk.andrewgorton.digitalmarketplace.alerter.User;
import uk.andrewgorton.digitalmarketplace.alerter.resources.UserResource;

import java.util.Arrays;

/**
 * Masks email composition and sending so that this is not directly called by resources.
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

    public void sendBidManagerEmail(Opportunity opportunity, String[] emailArray,
                                    String responseUrl) throws EmailException {

        if (!configuration.isEnabled()) {
            throw new EmailException("Email configuration not enabled");
        }

        HtmlEmail email = composer.composeBidManagerEmail(opportunity, responseUrl);
        Arrays.stream(emailArray)
                .map(String::trim)
                .distinct()
                .filter(validator::isValid)
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

    /**
     * Sends an email containing the provided url to the email address of the provided user, allowing them
     * to reset their password.
     *
     * @param user the user to send the password reset email to
     * @param url the password reset url, construct according to what
     * {@link UserResource#showResetPasswordView(long, String)} expects
     * @throws EmailException if the email could not be composed, or if there is no email configuration enabled
     */
    public void sendPasswordResetEmail(User user, String url) throws EmailException {
        if (!configuration.isEnabled()) {
            throw new EmailException("Email configuration not enabled");
        }

        HtmlEmail email = composer.composePasswordResetEmail(user, url);
        LOGGER.info("Sending email {} to {}", email.getSubject(), user.getEmail());
        email.send();
    }
}
