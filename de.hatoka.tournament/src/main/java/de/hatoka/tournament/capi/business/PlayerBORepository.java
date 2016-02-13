package de.hatoka.tournament.capi.business;

import java.util.List;

public interface PlayerBORepository
{
    /**
     * Creates a player without external reference.
     *
     * @param name
     * @return
     */
    PlayerBO create(String name);

    /**
     * Creates a player without external reference.
     *
     * @param name
     * @return
     */
    PlayerBO create(String externalRef, String name);

    PlayerBO getPlayerByID(String id);

    PlayerBO findByName(String name);

    PlayerBO findByExternalRef(String externalRef);

    List<PlayerBO> getPlayers();
}
