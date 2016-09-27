package uk.andrewgorton.digitalmarketplace.alerter.email;

import org.apache.commons.lang3.StringEscapeUtils;
import org.apache.commons.mail.EmailException;
import org.apache.commons.mail.HtmlEmail;
import uk.andrewgorton.digitalmarketplace.alerter.Alert;
import uk.andrewgorton.digitalmarketplace.alerter.Opportunity;

import java.util.ArrayList;
import java.util.List;

public class EmailAlerter {
    private final String host;
    private final int port;
    private final String from;
    private final HtmlEmailBodyFactory htmlEmailBodyFactory;
    private final TextEmailBodyFactory textEmailBodyFactory;
    private final boolean enabled;
    private final HtmlEmail htmlEmail;

    public EmailAlerter(String host,
                        int port,
                        String from,
                        HtmlEmailBodyFactory htmlEmailBodyFactory,
                        TextEmailBodyFactory textEmailBodyFactory,
                        boolean enabled) {
        this.host = host;
        this.port = port;
        this.from = from;
        this.htmlEmailBodyFactory = htmlEmailBodyFactory;
        this.textEmailBodyFactory = textEmailBodyFactory;
        this.enabled = enabled;
        htmlEmail = null;
    }

    // Unit test constructor
    public EmailAlerter(String host,
                        int port,
                        String from,
                        HtmlEmailBodyFactory htmlEmailBodyFactory,
                        TextEmailBodyFactory textEmailBodyFactory,
                        boolean enabled,
                        HtmlEmail htmlEmail) {
        this.host = host;
        this.port = port;
        this.from = from;
        this.htmlEmailBodyFactory = htmlEmailBodyFactory;
        this.textEmailBodyFactory = textEmailBodyFactory;
        this.enabled = enabled;
        this.htmlEmail = htmlEmail;
    }

    private HtmlEmail getHtmlEmail() {
        if (htmlEmail != null) {
            return htmlEmail;
        }
        return new HtmlEmail();
    }

    public boolean alert(Opportunity o, List<Alert> alerts) {
        boolean result = false;

        try {
            HtmlEmail email = getHtmlEmail();
            email.setHostName(host);
            email.setSmtpPort(port);
            email.setFrom(from);
            email.setSubject(String.format("DOS lead: %s",
                    StringEscapeUtils.unescapeHtml4(o.getCustomer())));

            email.setHtmlMsg(htmlEmailBodyFactory.create(o));
            email.setTextMsg(textEmailBodyFactory.create(o));

            // Filter out duplicate email addresses
            List<String> deduplicatedEmails = new ArrayList<>();
            alerts.forEach(singleAlert -> {
                if (!deduplicatedEmails.contains(singleAlert.getEmail())) {
                    deduplicatedEmails.add(singleAlert.getEmail());
                }
            });

            // Send an email
            deduplicatedEmails.forEach(singleEmail -> {
                try {
                    email.addTo(singleEmail);
                } catch (EmailException e) {
                    e.printStackTrace();
                }
            });

            // Send it
            if (enabled) {
                email.send();
            }
            result = true;
        } catch (EmailException e) {
            e.printStackTrace();
        }
        return result;
    }
}
