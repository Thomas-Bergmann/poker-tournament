package de.hatoka.mail.internal.service;

import java.util.Properties;

import javax.inject.Inject;
import javax.mail.Authenticator;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;

import de.hatoka.common.capi.configuration.SystemPropertyProvider;
import de.hatoka.mail.capi.config.SmtpConfiguration;

public class SmtpConfigurationSystemEnvImpl implements SmtpConfiguration
{
    private static final String SMTP_HOST = "SMTP_HOST";
    private static final String SMTP_USER = "SMTP_USER";
    private static final String SMTP_PASSWORD = "SMTP_PASSWORD";

    @Inject
    private SystemPropertyProvider system;

    @Override
    public Session getSession()
    {
        final Properties prop = new Properties();
        prop.setProperty("mail.smtp.host", system.get(SMTP_HOST));
        prop.setProperty("mail.smtp.auth", "true");
        Authenticator auth = null;
        if (system.get(SMTP_USER) != null)
        {
            auth = new Authenticator()
            {
                @Override
                protected PasswordAuthentication getPasswordAuthentication()
                {
                    return new PasswordAuthentication(system.get(SMTP_USER), system.get(SMTP_PASSWORD));
                }
            };
        }
        return Session.getInstance(prop, auth);
    }

}
