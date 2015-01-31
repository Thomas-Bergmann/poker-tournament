package de.hatoka.tournament.internal.config;

import java.net.URI;
import java.util.Properties;

import de.hatoka.tournament.capi.config.TournamentConfiguration;

/**
 * Bean configuration for tournament
 */
public class TournamentConfigurationSystemEnvImpl implements TournamentConfiguration
{
    private static final String ACCOUNT_LOGIN = "ACCOUNT_LOGIN";
    private static final String DATEFORMAT = "DATE_FORMAT";
    private static final String ACCOUNT_SECRET = "ACCOUNT_SECRET";

    @Override
    public String getDateFormat()
    {
        Properties prop = System.getProperties();
        String dateFormat = prop.getProperty(DATEFORMAT);
        return dateFormat == null ? "dd.mm.yyyy hh:MM" : dateFormat;
    }

    @Override
    public URI getLoginURI()
    {
        Properties prop = System.getProperties();
        return URI.create(prop.getProperty(ACCOUNT_LOGIN));
    }

    @Override
    public String getSecret()
    {
        Properties prop = System.getProperties();
        return prop.getProperty(ACCOUNT_SECRET);
    }
}
