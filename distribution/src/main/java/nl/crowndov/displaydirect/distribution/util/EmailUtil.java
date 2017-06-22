package nl.crowndov.displaydirect.distribution.util;

import nl.crowndov.displaydirect.distribution.Configuration;
import org.apache.commons.mail.DefaultAuthenticator;
import org.apache.commons.mail.Email;
import org.apache.commons.mail.EmailException;
import org.apache.commons.mail.HtmlEmail;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

/**
 * Copyright 2017 CROW-NDOV
 *
 * This file is subject to the terms and conditions defined in file 'LICENSE.txt', which is part of this source code package.
 */
public class EmailUtil {

    private static final Logger LOGGER = LoggerFactory.getLogger(EmailUtil.class);

    public static String getHtmlBody(String template, Map<String, Object> variables) {
        String html = TemplateUtil.renderTemplate("templates/"+template+".html.mustache", variables);
        if (html == null || html.isEmpty()) { // TODO: Null check or optional if template doesn't exist
             html = getTextBody(template, variables).replace("\n", "<br />");
        }
        return html;
    }

    public static String getTextBody(String template, Map<String, Object> body) {
        return TemplateUtil.renderTemplate("templates/"+template+".txt.mustache", body);
    }

    public static void sendHtmlEmail(String to, String subject, String template, Map<String, Object> variables) {

        try {
            final HtmlEmail email = new HtmlEmail();
            setupEmail(email, to, subject, template, variables);
            email.setHtmlMsg(EmailUtil.getHtmlBody(template, variables));
            email.setTextMsg(EmailUtil.getTextBody(template, variables));
            email.setSocketConnectionTimeout(Configuration.getSmtpConnectTimeout());

            email.send();
        } catch (Exception e) {
            LOGGER.error("Error sending HTML email", e);
        }
    }

    private static void setupEmail(Email mail, String to, String subject, String template, Map<String, Object> body) throws EmailException {
        mail.setHostName(Configuration.getSmtpHostname());
        mail.setFrom(Configuration.getSmtpFrom(), "DisplayDirect");
        if (Configuration.getSmtpUsername() != null && Configuration.getSmtpPassword() != null) {
            mail.setAuthenticator(new DefaultAuthenticator(Configuration.getSmtpUsername(), Configuration.getSmtpPassword()));
        }
        if (Configuration.getSmtpSsl()) {
            mail.setSSLOnConnect(true); // Setting this changes the port to 465
        }
        mail.setSmtpPort(Configuration.getSmtpPort());

        mail.addReplyTo(Configuration.getSmtpReplyTo(), "DisplayDirect");
        mail.setSubject(subject);
        addEmailTo(mail, to);
    }

    private static void addEmailTo(Email message, String address) {
        try {
            message.addTo(address);
        } catch (EmailException e1) {
            LOGGER.error("Error adding email address {}", address);
        }
    }

    private static void addEmailCc(Email message, String address) {
        try {
            message.addCc(address);
        } catch (EmailException e1) {
            LOGGER.error("Error adding email address {}", address);
        }
    }
}
