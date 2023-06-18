package de.hatoka.cashgame.capi.business;

import de.hatoka.cashgame.capi.types.CompetitorState;
import de.hatoka.common.capi.math.Money;
import de.hatoka.player.capi.business.PlayerBO;

public interface CompetitorBO
{
    /**
     * @return the amount of money spend by player (invest - payout)
     */
    Money getInPlay();

    /**
     * @return the player instance
     */
    PlayerBO getPlayer();

    /**
     * @return (Current) position of player.
     */
    Integer getPosition();

    default Money getResult()
    {
        return getInPlay().negate();
    }

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
     * Removes competitor from game
     */
    void remove();

    /**
      * Player activates the competitor, with paying the buy in.
      * @param money
      */
     void buyin(Money amount);

     /**
      * Player re-buys an amount, the amount will be added to the money in play.
      * @param reBuy
      */
     void rebuy(Money reBuy);

     /**
      * Player pay out with given money on table.
      * @param stack money on table
      * @return amount of win/lost money (- (buy-in + re-buy) + stack
      */
     Money payout(Money stack);

     /**
      * Player stand up (is inactive afterwards)
      */
     void standUp();

     /**
      * Player takes a seat (is inactive before and active afterwards)
      */
     void takeSeat();
}
