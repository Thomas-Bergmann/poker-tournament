package de.hatoka.tournament.capi.business;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import de.hatoka.common.capi.math.Money;
import de.hatoka.group.capi.business.GroupRef;
import de.hatoka.player.capi.business.HistoryEntryBO;
import de.hatoka.player.capi.business.PlayerBO;

public interface TournamentBO
{
    /**
     * @return reference to tournament
     */
    TournamentRef getRef();

    /**
     * @return the start date and time of the tournament
     */
    Date getStartTime();

    /**
     * set start date and time of the tournament
     */
    void setStartTime(Date date);

    /**
     * @return name of tournament
     */
    String getName();

    /**
     * Set name of tournament
     */
    void setName(String name);

    /**
     * Creates a new blind level for the tournament
     *
     * @param duration
     * @param smallBlind
     * @param bigBlind
     * @param ante
     * @return
     */
    BlindLevelBO createBlindLevel(int duration, int smallBlind, int bigBlind, int ante);

    /**
     * Creates a new blind level for the tournament
     *
     * @param duration
     * @return
     */
    PauseBO createPause(int duration);

    /**
     * @return rounds of tournament
     */
    List<TournamentRoundBO> getTournamentRounds();

    /**
     * @return current amount of rebuy (null if no rebuy possible)
     */
    Money getCurrentRebuy();

    /**
     * Defines the upper limit of players per table. This limit has an higher priority than the lower limit.
     *
     * @return maximum amount of player at one table
     */
    int getMaximumNumberOfPlayersPerTable();

    void setMaximumNumberOfPlayersPerTable(int number);

    /**
     * The initial stack size is used to convert the buy in value to chip amount. The ratio is used for re-buy, also.
     *
     * @return initial stack size per player
     */
    int getInitialStacksize();

    void setInitialStacksize(int initialStacksize);

    /**
     * Calculate the stack size of the winner. This includes all initial stacks plus re-buy stacks.
     *
     * @return stack size of the winner
     */
    int getFinalStacksize();

    /**
     * assign all active competitors to tables
     */
    void placePlayersAtTables();

    /**
     * @return tables with assigned competitors
     */
    List<TableBO> getTables();

    /**
     * @return get players, which are inactive but take part of tournament (which are placed)
     */
    List<CompetitorBO> getPlacedCompetitors();

    /**
     * Moves players from tables with more players to smaller tables.
     *
     * @return moved competitors
     */
    Collection<CompetitorBO> levelOutTables();

    /**
     * Registration can be done without buyin, a preparation step for the tournament
     *
     * @param playerBO
     * @return
     */
    CompetitorBO register(PlayerBO playerBO);

    /**
     * Player leaves the table and the tournament pays depends on rank (is inactive afterwards).
     *
     * @param competitorBO
     */
    void seatOpen(CompetitorBO competitorBO);

    /**
     * Creates a new rank
     *
     * @param firstPosition
     *            (mandatory)
     * @param lastPosition
     *            (optional)
     * @param percentage
     *            (optional)
     * @param amount
     *            of rank (optional)
     */
    RankBO createRank(int firstPosition, int lastPosition, BigDecimal percentage, BigDecimal amount);

    default RankBO createFixRank(int firstPosition, int lastPosition, BigDecimal amount)
    {
        return createRank(firstPosition, lastPosition, BigDecimal.ZERO, amount);
    }

    default RankBO createRank(int firstPosition, int lastPosition, BigDecimal percentage)
    {
        return createRank(firstPosition, lastPosition, percentage, null);
    }

    default RankBO createRankWithoutPrice(int firstPosition, int lastPosition)
    {
        return createRank(firstPosition, lastPosition, BigDecimal.ZERO, BigDecimal.ZERO);
    }

    List<RankBO> getRanks();

    /**
     * Starts the tournament (current round is set to 0), registration, modifications of ranks, blind levels is not
     * longer possible.
     */
    void start();

    /**
     * Defines the amount of rebuy, the currency is defined by the buy in. The {@link TournamentRoundBO} defines that a
     * rebuy is possible.
     *
     * @param rebuy
     *            amount
     */
    void setReBuy(BigDecimal rebuy);

    /**
     * @return the defined rebuy
     */
    Money getReBuy();

    /**
     * @return the current blind level
     *         <li>in case the current level is a pause it will return null
     *         <li>in case the tournament wasn't started yet, it returns the first blind level
     *         <li>in case the tournament is over, it return null
     */
    BlindLevelBO getCurrentBlindLevel();

    /**
     * @return the next blind level
     *         <li>in case the next level is a pause, it will return null
     *         <li>in case the tournament wasn't started yet, it returns the second blind level (in case it's pause then
     *         null)
     *         <li>in case the tournament is over, it return null
     */
    BlindLevelBO getNextBlindLevel();

    /**
     * @return next pause or null in case there is no pause
     */
    PauseBO getNextPause();

    GroupRef getGroupRef();

    void setGroupRef(GroupRef groupRef);

    void remove(TournamentRoundBO tournamentRoundBO);
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
