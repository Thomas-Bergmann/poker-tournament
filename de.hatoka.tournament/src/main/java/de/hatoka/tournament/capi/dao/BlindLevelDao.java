package de.hatoka.tournament.capi.dao;

import de.hatoka.common.capi.dao.Dao;
import de.hatoka.tournament.capi.entities.BlindLevelPO;
import de.hatoka.tournament.capi.entities.TournamentPO;

public interface BlindLevelDao extends Dao<BlindLevelPO>
{
    /**
     * Add blind level to tournament
     * @param tournamentPO
     * @param duration
     * @return
     */
    BlindLevelPO createAndInsert(TournamentPO tournamentPO, Integer duration);
}
