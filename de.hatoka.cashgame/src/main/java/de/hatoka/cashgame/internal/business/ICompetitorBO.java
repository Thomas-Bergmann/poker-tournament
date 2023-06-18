package de.hatoka.cashgame.internal.business;

import de.hatoka.cashgame.capi.business.CompetitorBO;

/**
 * This interface is used by game game implementation to set internal data of a competitor
 */
public interface ICompetitorBO extends CompetitorBO
{
    /**
     * Defines the position of the player. The position is independent from
     * "inPlayStatus"
     *
     * @param position
     */
    void setPosition(Integer position);
}
