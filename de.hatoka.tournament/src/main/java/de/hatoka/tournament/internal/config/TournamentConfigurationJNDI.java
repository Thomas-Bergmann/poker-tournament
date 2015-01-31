package de.hatoka.tournament.internal.config;

import java.net.URI;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import de.hatoka.common.capi.exceptions.IllegalConfigurationException;
import de.hatoka.jndi.account.ClientConfigurationBean;
import de.hatoka.tournament.capi.config.TournamentConfiguration;

/**
 * Bean configuration for tournament
 */
public class TournamentConfigurationJNDI implements TournamentConfiguration
{
    @Override
    public String getDateFormat()
    {
        ClientConfigurationBean bean = getConfigurationBean();
        return bean.getDateFormat() == null ? "dd.mm.yyyy hh:MM" : bean.getDateFormat();
    }

    @Override
    public URI getLoginURI()
    {
        ClientConfigurationBean bean = getConfigurationBean();
        return bean.getLoginURI();
    }

    private ClientConfigurationBean getConfigurationBean()
    {
        try
        {
            Context initCtx = new InitialContext();
            Context envCtx = (Context)initCtx.lookup("java:comp/env");
            ClientConfigurationBean bean = (ClientConfigurationBean)envCtx
                            .lookup("hatoka/ClientConfigurationBeanFactory");
            return bean;
        }
        catch(NamingException e)
        {
            throw new IllegalConfigurationException("Can't get client configuration for account application.", e);
        }
    }

    @Override
    public String getSecret()
    {
        ClientConfigurationBean bean = getConfigurationBean();
        return bean.getSecret();
    }
}
