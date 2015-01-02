package de.hatoka.tournament.capi.dao;

import de.hatoka.common.capi.dao.Dao;
import de.hatoka.tournament.capi.entities.CompetitorPO;
import de.hatoka.tournament.capi.entities.PlayerPO;
import de.hatoka.tournament.capi.entities.TournamentPO;

public interface CompetitorDao extends Dao<CompetitorPO>
{
    /**
     * Add player to tournament
     * @param tournamentPO
     * @param playerPO
     * @return
     */
    CompetitorPO createAndInsert(TournamentPO tournamentPO, PlayerPO playerPO);
}
