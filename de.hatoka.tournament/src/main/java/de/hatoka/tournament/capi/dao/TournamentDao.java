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
     * @return
     */
    TournamentPO createAndInsert(String accountRef, String name, Date date);

    List<TournamentPO> getByAccountRef(String accountRef);
}
