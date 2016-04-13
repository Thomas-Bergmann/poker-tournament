package de.hatoka.tournament.internal.dao;

import javax.inject.Inject;

import de.hatoka.common.capi.dao.GenericJPADao;
import de.hatoka.common.capi.dao.UUIDGenerator;
import de.hatoka.tournament.capi.dao.CompetitorDao;
import de.hatoka.tournament.capi.entities.CompetitorPO;
import de.hatoka.tournament.capi.entities.PlayerPO;
import de.hatoka.tournament.capi.entities.TournamentPO;
import de.hatoka.tournament.capi.types.CompetitorState;

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
        competitorPO.setState(CompetitorState.REGISTERED.name());
        competitorPO.setPlayer(playerPO.getName());
        // add relations
        playerPO.getCompetitors().add(competitorPO);
        tournamentPO.getCompetitors().add(competitorPO);
        // insert
        insert(competitorPO);
        return competitorPO;
    }


    @Override
    public void remove(CompetitorPO competitorPO)
    {
        // remove relations
        PlayerPO player = competitorPO.getPlayerPO();
        if (player != null)
        {
            player.getCompetitors().remove(competitorPO);
            competitorPO.setPlayerPO(null);
        }
        TournamentPO tournament = competitorPO.getTournamentPO();
        if (tournament != null)
        {
            tournament.getCompetitors().remove(competitorPO);
            competitorPO.setTournamentPO(null);
        }
        super.remove(competitorPO);
    }
}
