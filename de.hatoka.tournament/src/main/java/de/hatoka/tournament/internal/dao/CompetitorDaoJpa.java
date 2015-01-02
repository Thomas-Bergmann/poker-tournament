package de.hatoka.tournament.internal.dao;

import javax.inject.Inject;

import de.hatoka.common.capi.dao.GenericJPADao;
import de.hatoka.common.capi.dao.UUIDGenerator;
import de.hatoka.tournament.capi.dao.CompetitorDao;
import de.hatoka.tournament.capi.entities.CompetitorPO;
import de.hatoka.tournament.capi.entities.PlayerPO;
import de.hatoka.tournament.capi.entities.TournamentPO;

public class CompetitorDaoJpa extends GenericJPADao<CompetitorPO> implements CompetitorDao
{
    @Inject
    private UUIDGenerator uuidGenerator;

    public CompetitorDaoJpa()
    {
        super(CompetitorPO.class);
    }

    @Override
    public CompetitorPO createAndInsert(TournamentPO tournamentPO, PlayerPO playerPO)
    {
        CompetitorPO competitorPO = create();
        competitorPO.setId(uuidGenerator.generate());
        competitorPO.setPlayerPO(playerPO);
        competitorPO.setTournamentPO(tournamentPO);
        competitorPO.setAccountRef(tournamentPO.getAccountRef());
        competitorPO.setActive(false);
        // add relations
        playerPO.getCompetitors().add(competitorPO);
        tournamentPO.getCompetitors().add(competitorPO);
        insert(competitorPO);
        return competitorPO;
    }


    @Override
    public void remove(CompetitorPO competitorPO)
    {
        competitorPO.getPlayerPO().getCompetitors().remove(competitorPO);
        competitorPO.getTournamentPO().getCompetitors().remove(competitorPO);
        super.remove(competitorPO);
    }
}
