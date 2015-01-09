package de.hatoka.mail.internal.service;

import javax.mail.Session;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import de.hatoka.common.capi.exceptions.IllegalConfigurationException;
import de.hatoka.mail.capi.config.SmtpConfiguration;

public class SmtpConfigurationJndiImpl implements SmtpConfiguration
{
    @Override
    public Session getSession()
    {
        try
        {
            Context initCtx = new InitialContext();
            Context envCtx = (Context)initCtx.lookup("java:comp/env");
            return (Session)envCtx.lookup("mail/Session");
        }
        catch(NamingException e)
        {
            throw new IllegalConfigurationException("Can't get mail session from naming context.", e);
        }
    }

}
