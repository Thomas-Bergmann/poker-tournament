package de.hatoka.tournament.modules;

import com.google.inject.Binder;
import com.google.inject.Module;

import de.hatoka.tournament.capi.config.TournamentConfiguration;
import de.hatoka.tournament.internal.config.TournamentConfigurationSystemEnvImpl;

public class TournamentConfigurationModule implements Module
{

    @Override
    public void configure(Binder binder)
    {
        binder.bind(TournamentConfiguration.class).to(TournamentConfigurationSystemEnvImpl.class).asEagerSingleton();
    }
}
