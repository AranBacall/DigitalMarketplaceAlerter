package uk.andrewgorton.digitalmarketplace.alerter.email;

import org.apache.commons.lang3.StringEscapeUtils;
import org.apache.commons.mail.EmailException;
import org.apache.commons.mail.HtmlEmail;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import uk.andrewgorton.digitalmarketplace.alerter.Opportunity;
import uk.andrewgorton.digitalmarketplace.alerter.views.email.EmailViewRenderer;
import uk.andrewgorton.digitalmarketplace.alerter.views.email.alert.AlertHtmlView;
import uk.andrewgorton.digitalmarketplace.alerter.views.email.alert.AlertTextView;

import java.io.IOException;

/**
 * Responsible for composing {@link HtmlEmail}s
 */
public class EmailComposer {

    private static final Logger LOGGER = LoggerFactory.getLogger(EmailComposer.class);

    private final EmailConfiguration configuration;
    private final EmailViewRenderer renderer;

    public EmailComposer(EmailConfiguration configuration, EmailViewRenderer renderer) {
        this.configuration = configuration;
        this.renderer = renderer;
    }

    /**
     * Composes a new {@link HtmlEmail} from the provided {@link Opportunity}
     *
     * @param opportunity the opportunity that will form the content of this alert email
     * @return a new {@link HtmlEmail} that needs recipients, but is otherwise ready to send
     */
    public HtmlEmail composeAlert(Opportunity opportunity) throws EmailException {
        HtmlEmail email = new HtmlEmail();
        email.setHostName(configuration.getHost());
        email.setSmtpPort(configuration.getPort());
        email.setFrom(configuration.getFrom());
        email.setSubject(String.format("DOS lead: %s",
                StringEscapeUtils.unescapeHtml4(opportunity.getCustomer())));
        LOGGER.info("Composing email alert about opportunity {}", email.getSubject());
        AlertHtmlView htmlView = new AlertHtmlView(opportunity,
                configuration.getAdminName(), configuration.getAdminEmail());
        AlertTextView textView = new AlertTextView(opportunity,
                configuration.getAdminName(), configuration.getAdminEmail());

        try {
            email.setHtmlMsg(renderer.renderEmail(htmlView));
            email.setTextMsg(renderer.renderEmail(textView));
        } catch (IOException e) {
            LOGGER.error(e.getMessage(), e);
            throw new EmailException(String.format("Failed to compose alert about opportunity %s",
                            opportunity.getCustomer()), e);
        }

        return email;
    }

}
