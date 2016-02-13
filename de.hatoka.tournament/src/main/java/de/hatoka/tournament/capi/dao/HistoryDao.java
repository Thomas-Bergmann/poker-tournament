package de.hatoka.tournament.capi.dao;

import java.util.Date;

import de.hatoka.common.capi.dao.Dao;
import de.hatoka.tournament.capi.entities.HistoryPO;
import de.hatoka.tournament.capi.entities.TournamentPO;

public interface HistoryDao extends Dao<HistoryPO>
{
    /**
     * Add history entry to tournament
     * @param tournamentPO
     * @param playerPO
     * @param date
     * @return
     */
    HistoryPO createAndInsert(TournamentPO tournamentPO, String player, Date date);
}
