package de.hatoka.tournament.internal.business;

import javax.inject.Inject;

import de.hatoka.tournament.capi.business.CompetitorBO;
import de.hatoka.tournament.capi.business.PlayerBO;
import de.hatoka.tournament.capi.business.PlayerBORepository;
import de.hatoka.tournament.capi.business.TournamentBO;
import de.hatoka.tournament.capi.business.TournamentBORepository;
import de.hatoka.tournament.capi.business.TournamentBusinessFactory;
import de.hatoka.tournament.capi.dao.CompetitorDao;
import de.hatoka.tournament.capi.dao.PlayerDao;
import de.hatoka.tournament.capi.dao.TournamentDao;
import de.hatoka.tournament.capi.entities.CompetitorPO;
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

    @Override
    public CompetitorBO getCompetitorBO(CompetitorPO competitorPO)
    {
        return new CompetitorBOImpl(competitorPO, competitorDao, this);
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
        return new TournamentBORepositoryImpl(accountRef, tournamentDao, this);
    }

}
