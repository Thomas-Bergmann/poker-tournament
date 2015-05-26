package de.hatoka.tournament.capi.dao;

import de.hatoka.common.capi.dao.Dao;
import de.hatoka.tournament.capi.entities.RankPO;
import de.hatoka.tournament.capi.entities.TournamentPO;

public interface RankDao extends Dao<RankPO>
{
    /**
     * Add rank to tournament
     * @param tournamentPO
     * @param firstPosition
     * @return
     */
    RankPO createAndInsert(TournamentPO tournamentPO, Integer firstPosition);
}
