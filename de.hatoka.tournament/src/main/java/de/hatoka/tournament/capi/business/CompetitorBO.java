package de.hatoka.tournament.capi.business;

import de.hatoka.common.capi.business.Money;

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
     * Player activates the competitor, with paying the buy in.
     * @param money
     */
    void buyin(Money amount);

    /**
     * @return the amount of money spend by player (buy-in and re-buy)
     */
    Money getInPlay();

    /**
     * @return the player instance
     */
    PlayerBO getPlayerBO();

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
    boolean isActive();

    /**
     * Player re-buys an amount, the amount will be added to the money in play.
     * @param reBuy
     */
    void rebuy(Money reBuy);

    /**
     * Defines the position of the player. The position is independent from "inPlayStatus"
     * @param position
     */
    void setPosition(Integer position);
}
