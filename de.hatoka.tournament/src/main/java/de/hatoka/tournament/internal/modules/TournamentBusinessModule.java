package de.hatoka.tournament.internal.modules;

import com.google.inject.Binder;
import com.google.inject.Module;

import de.hatoka.tournament.capi.business.TournamentBusinessFactory;
import de.hatoka.tournament.capi.config.TournamentConfiguration;
import de.hatoka.tournament.internal.business.TournamentBusinessFactoryImpl;
import de.hatoka.tournament.internal.config.TournamentConfigurationJNDI;

public class TournamentBusinessModule implements Module
{

    @Override
    public void configure(Binder binder)
    {
        binder.bind(TournamentBusinessFactory.class).to(TournamentBusinessFactoryImpl.class).asEagerSingleton();
        binder.bind(TournamentConfiguration.class).to(TournamentConfigurationJNDI.class).asEagerSingleton();
    }
}
