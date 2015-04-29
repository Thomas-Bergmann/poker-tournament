package de.hatoka.tournament.capi.business;

import java.util.Date;

import de.hatoka.common.capi.business.Money;

public interface CashGameBO extends GameBO
{
    /**
     * @return amount of money for all active players.
     */
    Money getAverageInplay();

    Date getDate();

    String getName();

    /**
     * Player leaves the table and pays the given amount back. The tournament position is not modified.
     *
     * @param restAmount
     */
    void seatOpen(CompetitorBO competitor, Money restAmount);
}
