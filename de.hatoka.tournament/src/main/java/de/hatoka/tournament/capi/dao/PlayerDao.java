package de.hatoka.tournament.capi.dao;

import java.util.Collection;

import de.hatoka.common.capi.dao.Dao;
import de.hatoka.tournament.capi.entities.PlayerPO;

public interface PlayerDao extends Dao<PlayerPO>
{
    /**
     * @param accountRef
     * @param name
     *            of player
     * @return
     */
    public PlayerPO createAndInsert(String accountRef, String name);

    public PlayerPO findByName(String accountRef, String name);

    public Collection<PlayerPO> getByAccountRef(String accountRef);
}