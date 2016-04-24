package de.hatoka.tournament.modules;

import com.google.inject.Binder;
import com.google.inject.Module;

import de.hatoka.tournament.capi.business.TournamentBusinessFactory;
import de.hatoka.tournament.internal.business.TournamentBusinessFactoryImpl;

public class TournamentBusinessModule implements Module
{

    @Override
    public void configure(Binder binder)
    {
        binder.bind(TournamentBusinessFactory.class).to(TournamentBusinessFactoryImpl.class).asEagerSingleton();
    }
}
