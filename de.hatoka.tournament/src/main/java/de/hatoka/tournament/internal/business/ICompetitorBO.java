package de.hatoka.tournament.internal.business;

import de.hatoka.common.capi.math.Money;
import de.hatoka.tournament.capi.business.CompetitorBO;

/**
 * This interface is used by game game implementation to set internal data of a competitor
 */
public interface ICompetitorBO extends CompetitorBO
{
    void setInactive();

    /**
     * Defines the position of the player. The position is independent from
     * "inPlayStatus"
     *
     * @param position
     */
    void setPosition(Integer position);

    /**
     * player is placed at table number.
     *
     * @param tableNumber
     * @param position at table (0 is dealer)
     */
    void takeSeat(int tableNo, int seatNo);

    /**
     * Player is out on position and gets some money, hopefully.
     * @param result
     * @param position
     */
    void seatOpen(Money result, Integer position);
}
