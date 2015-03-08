package de.hatoka.tournament.capi.dao;

import java.util.Date;
import java.util.List;

import de.hatoka.common.capi.dao.Dao;
import de.hatoka.tournament.capi.entities.TournamentPO;

public interface TournamentDao extends Dao<TournamentPO>
{
    /**
     * @param accountRef
     * @param name
     *            name of tournament
     * @param isCashGame
     * @return
     */
    TournamentPO createAndInsert(String accountRef, String name, Date date, boolean isCashGame);

    List<TournamentPO> getByAccountRef(String accountRef);
}
