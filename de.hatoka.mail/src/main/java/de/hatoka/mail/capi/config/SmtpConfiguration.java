package de.hatoka.mail.capi.config;

import javax.mail.Session;

public interface SmtpConfiguration
{
    Session getSession();
}
