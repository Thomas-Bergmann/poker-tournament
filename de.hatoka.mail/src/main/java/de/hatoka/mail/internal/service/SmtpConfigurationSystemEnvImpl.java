package de.hatoka.mail.internal.service;

import java.util.Properties;

import javax.mail.Authenticator;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;

import de.hatoka.mail.capi.config.SmtpConfiguration;

public class SmtpConfigurationSystemEnvImpl implements SmtpConfiguration
{
    private static final String SMTP_USER = "mail.smtp.user";
    private static final String SMTP_PASSWORD = "mail.smtp.password";

    @Override
    public Session getSession()
    {
        Properties prop = System.getProperties();
        Authenticator auth = null;
        if (prop.getProperty(SMTP_USER) != null)
        {
            auth = new Authenticator()
            {
                @Override
                protected PasswordAuthentication getPasswordAuthentication()
                {
                    return new PasswordAuthentication(prop.getProperty(SMTP_USER), prop.getProperty(SMTP_PASSWORD));
                }
            };
        }
        return Session.getInstance(prop, auth);
    }

}
