package de.hatoka.tournament.internal.business;

import java.util.Date;

import javax.inject.Inject;

import com.google.inject.Provider;

import de.hatoka.tournament.capi.business.CompetitorBO;
import de.hatoka.tournament.capi.business.HistoryEntryBO;
import de.hatoka.tournament.capi.business.PlayerBO;
import de.hatoka.tournament.capi.business.PlayerBORepository;
import de.hatoka.tournament.capi.business.TournamentBO;
import de.hatoka.tournament.capi.business.TournamentBORepository;
import de.hatoka.tournament.capi.business.TournamentBusinessFactory;
import de.hatoka.tournament.capi.dao.CompetitorDao;
import de.hatoka.tournament.capi.dao.HistoryDao;
import de.hatoka.tournament.capi.dao.PlayerDao;
import de.hatoka.tournament.capi.dao.TournamentDao;
import de.hatoka.tournament.capi.entities.CompetitorPO;
import de.hatoka.tournament.capi.entities.HistoryPO;
import de.hatoka.tournament.capi.entities.PlayerPO;
import de.hatoka.tournament.capi.entities.TournamentPO;

public class TournamentBusinessFactoryImpl implements TournamentBusinessFactory
{
    @Inject
    private TournamentDao tournamentDao;

    @Inject
    private PlayerDao playerDao;

    @Inject
    private CompetitorDao competitorDao;

    @Inject
    private HistoryDao historyDao;

    @Inject
    private Provider<Date> dateProvider;

    @Override
    public CompetitorBO getCompetitorBO(CompetitorPO competitorPO, TournamentBO tournamentBO)
    {
        return new CompetitorBOImpl(competitorPO, tournamentBO, historyDao, dateProvider, this);
    }

    @Override
    public PlayerBO getPlayerBO(PlayerPO playerPO)
    {
        return new PlayerBOImpl(playerPO, playerDao);
    }

    @Override
    public PlayerBORepository getPlayerBORepository(String accountRef)
    {
        return new PlayerBORepositoryImpl(accountRef, playerDao, this);
    }

    @Override
    public TournamentBO getTournamentBO(TournamentPO tournamentPO)
    {
        return new TournamentBOImpl(tournamentPO, tournamentDao, competitorDao, playerDao, this);
    }

    @Override
    public TournamentBORepository getTournamentBORepository(String accountRef)
    {
        return new TournamentBORepositoryImpl(accountRef, tournamentDao, playerDao, competitorDao, this);
    }

    @Override
    public HistoryEntryBO getHistoryBO(HistoryPO historyPO)
    {
        return new HistoryEntryBOImpl(historyPO, this);
    }

}
