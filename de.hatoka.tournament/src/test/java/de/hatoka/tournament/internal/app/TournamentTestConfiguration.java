package de.hatoka.tournament.internal.app;

import java.net.URI;

import de.hatoka.tournament.capi.config.TournamentConfiguration;

/**
 * Provides an URI for test cases.
 */
public class TournamentTestConfiguration implements TournamentConfiguration
{

    @Override
    public String getDateFormat()
    {
        return "dd.mm.yyyy hh:MM";
    }

    @Override
    public URI getLoginURI()
    {
        return URI.create("http://localhost:8090/test/account/login/index.html");
    }

}
