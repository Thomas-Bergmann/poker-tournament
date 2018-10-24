package de.hatoka.tournament.capi.business;

import de.hatoka.common.capi.math.Money;
import de.hatoka.player.capi.business.PlayerBO;
import de.hatoka.tournament.capi.types.CompetitorState;

/**
 * A competitor is an player attending at a tournament.
 */
public interface CompetitorBO
{
    /**
     * @return the amount of money spend by player (buy-in and re-buy)
     */
    Money getInPlay();

    /**
     * @return the player instance
     */
    PlayerBO getPlayer();

    /**
     * @return (Current) tournament position of player.
     */
    Integer getPosition();

    /**
     * @return the amount of money won by player after going out. (without investment)
     */
    Money getResult();

    /**
     * Player is in.
     *
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
     * @return table number the player is sitting
     */
    Integer getTableNo();

    /**
     * @return seat number the player is sitting
     */
    Integer getSeatNo();

    /**
     * Removes competitor from game
     */
    void remove();

    /**
     * Competitor pays the buy-in and is allowed to play (is active afterwards)
     *
     * @param competitorBO
     */
    void buyin();

    /**
     * Competitor pays additional re-buy and is allowed to play (is still active)
     * Rebuy amount is defined by tournament and/or round.
     */
    void rebuy();
}
