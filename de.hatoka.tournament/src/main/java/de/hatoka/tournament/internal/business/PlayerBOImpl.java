package de.hatoka.tournament.internal.business;

import de.hatoka.tournament.capi.business.PlayerBO;
import de.hatoka.tournament.capi.dao.PlayerDao;
import de.hatoka.tournament.capi.entities.PlayerPO;

public class PlayerBOImpl implements PlayerBO
{
    private PlayerPO playerPO;
    private final PlayerDao playerDao;

    public PlayerBOImpl(PlayerPO playerPO, PlayerDao playerDao)
    {
        this.playerPO = playerPO;
        this.playerDao = playerDao;
    }

    @Override
    public String getID()
    {
        return playerPO.getId();
    }

    @Override
    public String getName()
    {
        return playerPO.getName();
    }

    @Override
    public void remove()
    {
        playerDao.remove(playerPO);
        playerPO = null;
    }

}
