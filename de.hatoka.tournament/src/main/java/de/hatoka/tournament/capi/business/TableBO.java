package de.hatoka.tournament.capi.business;

import java.util.List;

public interface TableBO
{
    /**
     * @return the number of this table unique for a tournament
     */
    int getTableNo();

    /**
     * @return players sitting at the table the position of the list is the position at the table
     */
    List<CompetitorBO> getCompetitors();

    /**
     * Adds competitor to current table (at last position)
     * @param competitor
     */
    void addCompetitor(CompetitorBO competitor);
}
