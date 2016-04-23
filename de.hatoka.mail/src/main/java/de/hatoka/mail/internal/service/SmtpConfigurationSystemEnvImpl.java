package de.hatoka.mail.internal.service;

import java.util.Properties;

import javax.mail.Authenticator;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;

import de.hatoka.mail.capi.config.SmtpConfiguration;

public class SmtpConfigurationSystemEnvImpl implements SmtpConfiguration
{
    private static final String SMTP_HOST = "SMTP_HOST";
    private static final String SMTP_USER = "SMTP_USER";
    private static final String SMTP_PASSWORD = "SMTP_PASSWORD";

    @Override
    public Session getSession()
    {
        final Properties prop = System.getProperties();
        prop.setProperty("mail.smtp.host", System.getenv(SMTP_HOST));
        prop.setProperty("mail.smtp.auth", "true");
        Authenticator auth = null;
        if (System.getenv(SMTP_USER) != null)
        {
            auth = new Authenticator()
            {
                @Override
                protected PasswordAuthentication getPasswordAuthentication()
                {
                    return new PasswordAuthentication(System.getenv(SMTP_USER), System.getenv(SMTP_PASSWORD));
                }
            };
        }
        return Session.getInstance(prop, auth);
    }

}
