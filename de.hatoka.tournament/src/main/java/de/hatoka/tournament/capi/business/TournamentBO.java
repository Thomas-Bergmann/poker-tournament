package de.hatoka.tournament.capi.business;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.Date;
import java.util.List;

public interface TournamentBO extends GameBO
{
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
    TournamentRoundBO createPause(int duration);

    /**
     * @return rounds of tournament
     */
    List<TournamentRoundBO> getBlindLevels();

    /**
     * Removes a previously created pause or blind level
     * @param round
     */
    void remove(TournamentRoundBO round);

    int getMininumNumberOfPlayersPerTable();
    void setMininumNumberOfPlayersPerTable(int number);

    int getMaximumNumberOfPlayersPerTable();
    void setMaximumNumberOfPlayersPerTable(int number);

    void placePlayersAtTables();
    Collection<TableBO> getTables();

    /**
     * Registration can be done without buyin, a preparation step for the tournament
     *
     * @param playerBO
     * @return
     */
    CompetitorBO register(PlayerBO playerBO);

    /**
     * Competitor pays the buy-in and is allowed to play (is active afterwards)
     * @param competitorBO
     */
    void buyin(CompetitorBO competitorBO);

    /**
     * Competitor pays additional re-buy and is allowed to play (is still active)
     * @param competitorBO
     */
    void rebuy(CompetitorBO competitorBO);

    /**
     * Player leaves the table and the tournament pays depends on rank (is inactive afterwards).
     *
     * @param competitorBO
     */
    void seatOpen(CompetitorBO competitorBO);

    /**
     * Creates a new rank
     * @param firstPosition (mandatory)
     * @param lastPosition (optional)
     * @param percentage (optional)
     * @param amount (optional)
     */
    RankBO createRank(int firstPosition, int lastPosition, BigDecimal percentage, BigDecimal amount);
    default RankBO createFixRank(int firstPosition, int lastPosition, BigDecimal amount)
    {
        return createRank(firstPosition, lastPosition, null, amount);
    }

    default RankBO createRank(int firstPosition, int lastPosition, BigDecimal percentage)
    {
        return createRank(firstPosition, lastPosition, percentage, null);
    }

    default RankBO createRank(int firstPosition, int lastPosition)
    {
        return createRank(firstPosition, lastPosition, null, null);
    }

    List<RankBO> getRanks();

    void remove(RankBO rank);
}
