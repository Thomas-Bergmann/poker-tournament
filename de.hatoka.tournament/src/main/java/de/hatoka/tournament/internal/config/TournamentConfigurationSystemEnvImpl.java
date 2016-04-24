package de.hatoka.tournament.internal.config;

import java.net.URI;

import javax.inject.Inject;

import org.slf4j.LoggerFactory;

import de.hatoka.common.capi.configuration.SystemPropertyProvider;
import de.hatoka.tournament.capi.config.TournamentConfiguration;

/**
 * Bean configuration for tournament
 */
public class TournamentConfigurationSystemEnvImpl implements TournamentConfiguration
{
    private static final String ACCOUNT_LOGIN = "ACCOUNT_LOGIN";
    private static final String DATEFORMAT = "DATE_FORMAT";
    private static final String ACCOUNT_SECRET = "ACCOUNT_SECRET";

    @Inject
    private SystemPropertyProvider system;

    @Override
    public String getDateFormat()
    {
        String dateFormat = system.get(DATEFORMAT);
        return dateFormat == null ? "dd.mm.yyyy hh:MM" : dateFormat;
    }

    @Override
    public URI getLoginURI()
    {
        String loginURI = system.get(ACCOUNT_LOGIN);
        try
        {
            return new URI(loginURI);
        }
        catch(Exception e)
        {
            LoggerFactory.getLogger(getClass()).error("Can't convert string '"+loginURI+"' to URI.", e);
            return null;
        }
    }

    @Override
    public String getSecret()
    {
        return system.get(ACCOUNT_SECRET);
    }
}
