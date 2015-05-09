package de.hatoka.tournament.capi.business;

import de.hatoka.common.capi.business.Money;

public interface CashGameCompetitorBO extends CompetitorBO
{
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

}
