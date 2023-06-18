package de.hatoka.player.capi.business;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

import de.hatoka.user.capi.business.UserRef;

public interface PlayerBORepository
{
    /**
     * Creates a player in context of a user.
     * @param userRef user reference
     * @param playerRef player reference for user
     * @param name nick name of player
     * @return created player
     */
    PlayerBO createPlayer(PlayerRef playerRef, String name);

    /**
     * Find a player in context of a user.
     * @param userRef user reference
     * @param playerRef player reference for user
     * @return player
     */
    Optional<PlayerBO> findPlayer(PlayerRef playerRef);

    /**
     * @param userRef user reference
     * @return list of players known by user
     */
    List<PlayerBO> getPlayers(UserRef userRef);

    /**
     * Removes all users of this repository
     */
    default void clear()
    {
        getAllPlayers().forEach(PlayerBO::remove);
    }

    /**
     * @return all registered players
     */
    Collection<PlayerBO> getAllPlayers();
}
