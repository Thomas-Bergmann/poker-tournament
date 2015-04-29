package de.hatoka.tournament.capi.business;

import java.util.Collection;
import java.util.List;

import de.hatoka.common.capi.business.Money;

public interface GameBO
{
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

    Collection<CompetitorBO> getCompetitors();

    boolean isCompetitor(PlayerBO player);

    /**
     * Sort competitors recalculates the position of competitors.
     */
    void sortCompetitors();

    Money getBuyIn();

    void setBuyIn(Money instance);


    /**
     * @return amount of money for all active players.
     */
    Money getSumInplay();

    /**
     *
     * @return ordered list of history entries
     */
    List<HistoryEntryBO> getHistoryEntries();
}
