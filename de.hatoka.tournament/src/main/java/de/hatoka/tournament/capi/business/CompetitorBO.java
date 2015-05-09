package de.hatoka.tournament.capi.business;

import de.hatoka.common.capi.business.Money;
import de.hatoka.tournament.capi.types.CompetitorState;

/**
 * A competitor is an player attending at a tournament.
 */
public interface CompetitorBO
{
   /**
    *
    * @return the identifier (signature) of the business object
    */
   String getID();

    /**
     * @return the amount of money spend by player (buy-in and re-buy)
     */
    Money getInPlay();

    /**
     * @return the player instance
     */
    PlayerBO getPlayer();

    /**
     * (Current) tournament position of player.
     *
     * @return
     */
    Integer getPosition();

    /**
     * @return the amount of money won or lost by player after going out.
     */
    Money getResult();

    /**
     * Player is in.
     * @return
     */
    CompetitorState getState();

    /**
     * @return true if player is active, means can playing cards
     */
    default boolean isActive()
    {
        return CompetitorState.ACTIVE.equals(getState());
    }

    /**
     * Defines the position of the player. The position is independent from "inPlayStatus"
     * @param position
     */
    void setPosition(Integer position);
}
