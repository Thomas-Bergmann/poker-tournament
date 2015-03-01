package de.hatoka.tournament.capi.business;

import java.util.Collection;
import java.util.Date;

import de.hatoka.common.capi.business.Money;

public interface TournamentBO
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
     * A player likes to attend to the tournament, the buy-in is not paid.
     *
     * @param playerBO
     * @return
     */
    CompetitorBO assign(PlayerBO playerBO);

    /**
     * Reverse action of assign. The competitor must be inactive (use {@link CompetitorBO#seatOpen(Money)} and requires a zero result.
     *
     * @param competitorBO
     */
    void unassign(CompetitorBO competitorBO);

    Collection<CompetitorBO> getActiveCompetitors();

    /**
     * @return amount of money for all active players.
     */
    Money getAverageInplay();

    Money getBuyIn();

    Collection<CompetitorBO> getCompetitors();

    Date getDate();

    String getName();

    /**
     * @return amount of money for all active players.
     */
    Money getSumInplay();

    boolean isCompetitor(PlayerBO player);

    void setBuyIn(Money instance);

    /**
     * Sort competitors recalculates the position of competitors.
     */
    void sortCompetitors();
}
