package de.hatoka.tournament.capi.business;

import java.util.Collection;
import java.util.List;

import de.hatoka.common.capi.business.Money;

public interface GameBO
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
     * @return active players
     */
    Collection<CompetitorBO> getActiveCompetitors();

    /**
     * @return all players (active, inactive and registered)
     */
    Collection<CompetitorBO> getCompetitors();

    /**
     * @param player
     * @return true if player is registered or more
     */
    boolean isCompetitor(PlayerBO player);

    /**
     * Removes registration of player
     *
     * @param competitorBO
     */
    void unassign(CompetitorBO competitorBO);

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
