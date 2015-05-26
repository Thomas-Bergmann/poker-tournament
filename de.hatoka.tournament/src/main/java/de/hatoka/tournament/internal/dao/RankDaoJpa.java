package de.hatoka.tournament.internal.dao;

import javax.inject.Inject;

import de.hatoka.common.capi.dao.GenericJPADao;
import de.hatoka.common.capi.dao.UUIDGenerator;
import de.hatoka.tournament.capi.dao.RankDao;
import de.hatoka.tournament.capi.entities.RankPO;
import de.hatoka.tournament.capi.entities.TournamentPO;

public class RankDaoJpa extends GenericJPADao<RankPO> implements RankDao
{
    @Inject
    private UUIDGenerator uuidGenerator;

    public RankDaoJpa()
    {
        super(RankPO.class);
    }

    @Override
    public RankPO createAndInsert(TournamentPO tournamentPO, Integer firstPosition)
    {
        RankPO result = create();
        result.setId(uuidGenerator.generate());
        result.setFirstPosition(firstPosition);
        // set relations
        result.setTournament(tournamentPO);
        tournamentPO.getRanks().add(result);
        insert(result);
        return result;
    }


    @Override
    public void remove(RankPO rankPO)
    {
        TournamentPO tournament = rankPO.getTournament();
        if (tournament != null)
        {
            tournament.getRanks().remove(rankPO);
            rankPO.setTournament(null);
        }
        super.remove(rankPO);
    }
}
