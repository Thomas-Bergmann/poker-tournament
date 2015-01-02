package de.hatoka.tournament.internal.modules;

import com.google.inject.Binder;
import com.google.inject.Module;

import de.hatoka.tournament.capi.dao.CompetitorDao;
import de.hatoka.tournament.capi.dao.PlayerDao;
import de.hatoka.tournament.capi.dao.TournamentDao;
import de.hatoka.tournament.internal.dao.CompetitorDaoJpa;
import de.hatoka.tournament.internal.dao.PlayerDaoJpa;
import de.hatoka.tournament.internal.dao.TournamentDaoJpa;

public class TournamentDaoJpaModule implements Module
{
    @Override
    public void configure(Binder binder)
    {
        binder.bind(TournamentDao.class).to(TournamentDaoJpa.class).asEagerSingleton();
        binder.bind(PlayerDao.class).to(PlayerDaoJpa.class).asEagerSingleton();
        binder.bind(CompetitorDao.class).to(CompetitorDaoJpa.class).asEagerSingleton();
    }
}
