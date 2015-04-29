package de.hatoka.tournament.capi.business;

import java.util.Date;

import de.hatoka.common.capi.business.Money;

public interface CashGameBO extends GameBO
{
    /**
     * @return the identifier (artificial key)
     */
    String getID();

    /**
     * Removes that object
     */
    void remove();

    /**
     * @return amount of money for all active players.
     */
    Money getAverageInplay();

    Date getDate();

    String getName();
}
