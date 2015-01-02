package de.hatoka.tournament.capi.business;

import java.util.List;

public interface PlayerBORepository
{
    PlayerBO create(String name);

    PlayerBO findByName(String name);

    public PlayerBO getByID(String id);

    List<PlayerBO> getPlayerBOs();
}
