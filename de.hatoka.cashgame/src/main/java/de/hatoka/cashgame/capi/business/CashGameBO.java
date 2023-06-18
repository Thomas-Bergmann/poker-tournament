package de.hatoka.cashgame.capi.business;

import java.util.Collection;
import java.util.Date;
import java.util.List;

import de.hatoka.common.capi.math.Money;
import de.hatoka.player.capi.business.HistoryEntryBO;
import de.hatoka.player.capi.business.PlayerBO;

public interface CashGameBO
{
    CashGameRef getRef();

    /**
     * A player likes to attend to the game game, the buy-in is paid.
     *
     * @param playerBO
     */
    CompetitorBO sitDown(PlayerBO playerBO, Money buyIn);

    /**
     * @return amount of money for all active players.
     */
    Money getAverageInplay();

    Date getDate();

    /**
     * @return competitors of cash game
     */
    Collection<CompetitorBO> getCashGameCompetitors();
    /**
     * Removes that object
     */
    void remove();

    /**
     * @return active players
     */
    Collection<CompetitorBO> getActiveCompetitors();

    /**
     * @return all players (active, inactive)
     */
    Collection<CompetitorBO> getCompetitors();

    /**
     * @param player
     * @return true if player is registered or more
     */
    boolean isCompetitor(PlayerBO player);

    /**
     * Removes registration of player without actions
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
