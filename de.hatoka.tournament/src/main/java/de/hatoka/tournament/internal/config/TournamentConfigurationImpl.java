package de.hatoka.tournament.internal.config;

import java.net.URI;

import de.hatoka.tournament.capi.config.TournamentConfiguration;

/**
 * Straight forward implementation
 * TODO must be configurable for deployment
 */
public class TournamentConfigurationImpl implements TournamentConfiguration
{

    @Override
    public String getDateFormat()
    {
        return "dd.mm.yyyy hh:MM";
    }

    @Override
    public URI getLoginURI()
    {
        return URI.create("http://localhost:8090/de.hatoka.account-0.1/account/login/index.html");
    }

}
