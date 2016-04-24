package de.hatoka.user.internal.config;

import javax.mail.Session;

import de.hatoka.mail.capi.config.SmtpConfiguration;

public class SmtpTestConfiguration implements SmtpConfiguration
{

    @Override
    public Session getSession()
    {
        return null;
    }
}
