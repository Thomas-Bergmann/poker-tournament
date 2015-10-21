package de.hatoka.tournament.internal.business;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Currency;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import de.hatoka.common.capi.business.Money;
import de.hatoka.tournament.capi.business.BlindLevelBO;
import de.hatoka.tournament.capi.business.CompetitorBO;
import de.hatoka.tournament.capi.business.HistoryEntryBO;
import de.hatoka.tournament.capi.business.PlayerBO;
import de.hatoka.tournament.capi.business.RankBO;
import de.hatoka.tournament.capi.business.TableBO;
import de.hatoka.tournament.capi.business.TournamentBO;
import de.hatoka.tournament.capi.business.TournamentBusinessFactory;
import de.hatoka.tournament.capi.business.TournamentRoundBO;
import de.hatoka.tournament.capi.dao.BlindLevelDao;
import de.hatoka.tournament.capi.dao.CompetitorDao;
import de.hatoka.tournament.capi.dao.PlayerDao;
import de.hatoka.tournament.capi.dao.RankDao;
import de.hatoka.tournament.capi.dao.TournamentDao;
import de.hatoka.tournament.capi.entities.BlindLevelPO;
import de.hatoka.tournament.capi.entities.CompetitorPO;
import de.hatoka.tournament.capi.entities.HistoryPO;
import de.hatoka.tournament.capi.entities.PlayerPO;
import de.hatoka.tournament.capi.entities.RankPO;
import de.hatoka.tournament.capi.entities.TournamentPO;
import de.hatoka.tournament.capi.types.CompetitorState;
import de.hatoka.tournament.capi.types.HistoryEntryType;

public class TournamentBOImpl implements TournamentBO
{
    private TournamentPO tournamentPO;
    private final TournamentDao tournamentDao;
    private final CompetitorDao competitorDao;
    private final PlayerDao playerDao;
    private final BlindLevelDao blindLevelDao;
    private final RankDao rankDao;
    private final TournamentBusinessFactory factory;

    public TournamentBOImpl(TournamentPO tournamentPO, TournamentDao tournamentDao, CompetitorDao competitorDao, PlayerDao playerDao, BlindLevelDao blindLevelDao, RankDao rankDao,
                    TournamentBusinessFactory factory)
    {
        this.tournamentPO = tournamentPO;
        this.tournamentDao = tournamentDao;
        this.competitorDao = competitorDao;
        this.playerDao = playerDao;
        this.blindLevelDao = blindLevelDao;
        this.rankDao = rankDao;
        this.factory = factory;
    }

    @Override
    public CompetitorBO register(PlayerBO playerBO)
    {
        PlayerPO playerPO = playerDao.getById(playerBO.getID());
        if (playerPO == null)
        {
            throw new IllegalArgumentException("Can't resolve persistent object for playerBO:" + playerBO.getID());
        }
        return getBO(competitorDao.createAndInsert(tournamentPO, playerPO));
    }

    @Override
    public void buyin(CompetitorBO competitorBO)
    {
        if (competitorBO.isActive() || !(competitorBO instanceof ICompetitor))
        {
            throw new IllegalStateException("buyin not allowed for active competitors");
        }
        ICompetitor iTournamentCompetitor = (ICompetitor)competitorBO;
        iTournamentCompetitor.buyin(getBuyIn());
    }

    @Override
    public void rebuy(CompetitorBO competitorBO)
    {
        if (!competitorBO.isActive() || !(competitorBO instanceof ICompetitor))
        {
            throw new IllegalStateException("rebuy not allowed at inactive competitors");
        }
        ICompetitor iTournamentCompetitor = (ICompetitor)competitorBO;
        iTournamentCompetitor.rebuy(getCurrentRebuy());
    }

    @Override
    public Money getCurrentRebuy()
    {
        final List<TournamentRoundBO> blindLevels = getBlindLevels();
        int currentRound = tournamentPO.getCurrentRound();
        if (currentRound < 0 || blindLevels.size() <= currentRound)
        {
            return null;
        }
        return blindLevels.get(currentRound).getReBuy();
    }

    @Override
    public void seatOpen(CompetitorBO competitorBO)
    {
        if (!competitorBO.isActive() || !(competitorBO instanceof ICompetitor))
        {
            throw new IllegalStateException("seatOpen not allowed at inactive competitors");
        }
        int position = getActiveCompetitors().size();
        Money moneyResult = getResultForPosition(position);
        ICompetitor iTournamentCompetitor = (ICompetitor)competitorBO;
        iTournamentCompetitor.setInactive();
        iTournamentCompetitor.setResult(moneyResult);
        iTournamentCompetitor.setPosition(position);
        iTournamentCompetitor.createEntry(HistoryEntryType.CashOut, moneyResult);
    }

    private Money getResultForPosition(int position)
    {
        List<RankBO> assignedRanks = getRanks().stream().filter(rankBO -> rankBO.getFirstPosition() <= position && position <= rankBO.getLastPosition()).collect(Collectors.toList());
        if (assignedRanks.isEmpty())
        {
            return Money.NOTHING;
        }
        return assignedRanks.get(0).getAmountPerPlayer();
    }

    @Override
    public List<CompetitorBO> getActiveCompetitors()
    {
        return getActiveCompetitorBOStream().collect(Collectors.toList());
    }

    private Stream<CompetitorBO> getActiveCompetitorBOStream()
    {
        return getCompetitorBOStream().filter(competitor -> competitor.isActive());
    }

    private CompetitorBO getBO(CompetitorPO competitorPO)
    {
        return factory.getCompetitorBO(competitorPO, this);
    }

    @Override
    public Money getBuyIn()
    {
        return Money.valueOf(tournamentPO.getBuyIn());
    }

    @Override
    public List<CompetitorBO> getCompetitors()
    {
        return getCompetitorBOStream().sorted(CompetitorBOComparators.TOURNAMENT).collect(Collectors.toList());
    }

    private Stream<CompetitorBO> getCompetitorBOStream()
    {
        return tournamentPO.getCompetitors().stream().map(competitorPO -> getBO(competitorPO));
    }

    @Override
    public Date getStartTime()
    {
        return tournamentPO.getDate();
    }

    @Override
    public String getID()
    {
        return tournamentPO.getId();
    }

    @Override
    public String getName()
    {
        return tournamentPO.getName();
    }

    @Override
    public Money getSumInplay()
    {
        Money sum = Money.NOTHING;
        for (CompetitorBO competitor : getCompetitors())
        {
            sum = sum.add(competitor.getInPlay());
        }
        return sum;
    }

    @Override
    public boolean isCompetitor(PlayerBO player)
    {
        String playerID = player.getID();
        return tournamentPO.getCompetitors().stream().anyMatch(competitorPO -> competitorPO.getPlayerPO().getId().equals(playerID));
    }

    @Override
    public void remove()
    {
        tournamentDao.remove(tournamentPO);
        tournamentPO = null;
    }

    @Override
    public void setBuyIn(Money buyIn)
    {
        tournamentPO.setBuyIn(buyIn.toMoneyPO());
    }

    @Override
    public void unassign(CompetitorBO competitorBO)
    {
        if (competitorBO.isActive() || !competitorBO.getResult().isZero())
        {
            throw new IllegalStateException("Can't remove competitor, is/was in play with result.");
        }
        competitorDao.remove(competitorBO.getID());
    }

    @Override
    public int hashCode()
    {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((tournamentPO == null) ? 0 : tournamentPO.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj)
    {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        TournamentBOImpl other = (TournamentBOImpl)obj;
        if (tournamentPO == null)
        {
            if (other.tournamentPO != null)
                return false;
        }
        else if (!tournamentPO.equals(other.tournamentPO))
            return false;
        return true;
    }

    /**
     * Sort competitors recalculates the position of competitors.
     */
    @Override
    public void sortCompetitors()
    {
        // nothing to do at tournament
    }

    @Override
    public List<HistoryEntryBO> getHistoryEntries()
    {
        List<HistoryEntryBO> result = new ArrayList<>();
        for (HistoryPO historyPO : tournamentPO.getHistoryEntries())
        {
            result.add(factory.getHistoryBO(historyPO));
        }
        return result;
    }

    @Override
    public void setStartTime(Date date)
    {
        tournamentPO.setDate(date);
    }

    @Override
    public BlindLevelBO createBlindLevel(int duration, int smallBlind, int bigBlind, int ante, BigDecimal rebuyAmount)
    {
        BlindLevelPO blindLevelPO = blindLevelDao.createAndInsert(tournamentPO, duration);
        blindLevelPO.setPause(false);
        blindLevelPO.setSmallBlind(smallBlind);
        blindLevelPO.setBigBlind(bigBlind);
        blindLevelPO.setAnte(ante);
        blindLevelPO.setRebuyAmount(rebuyAmount);
        return factory.getBlindLevelBO(blindLevelPO, getCurrency());
    }

    private Currency getCurrency()
    {
        return getBuyIn().getCurrency();
    }

    @Override
    public TournamentRoundBO createPause(int duration)
    {
        BlindLevelPO blindLevelPO = blindLevelDao.createAndInsert(tournamentPO, duration);
        blindLevelPO.setPause(true);
        return factory.getPauseBO(blindLevelPO, getCurrency());
    }

    @Override
    public void setName(String name)
    {
        tournamentPO.setName(name);
    }

    @Override
    public List<TournamentRoundBO> getBlindLevels()
    {
        List<BlindLevelPO> blindLevels = tournamentPO.getBlindLevels();
        List<TournamentRoundBO> result = new ArrayList<TournamentRoundBO>(blindLevels.size());
        Currency currency = getCurrency();
        for (BlindLevelPO blindLevelPO : blindLevels)
        {
            if (blindLevelPO.isPause())
            {
                result.add(factory.getPauseBO(blindLevelPO, currency));
            }
            else
            {
                result.add(factory.getBlindLevelBO(blindLevelPO, currency));
            }
        }
        return result;
    }

    @Override
    public void remove(TournamentRoundBO round)
    {
        Iterator<BlindLevelPO> blindLevels = tournamentPO.getBlindLevels().iterator();
        while(blindLevels.hasNext())
        {
            BlindLevelPO blindLevelPO = blindLevels.next();
            if (blindLevelPO.getId().equals(round.getID()))
            {
                blindLevelDao.remove(blindLevelPO);
                break;
            }
        }
    }

    @Override
    public int getMaximumNumberOfPlayersPerTable()
    {
        return tournamentPO.getMaxPlayerPerTable();
    }

    @Override
    public void setMaximumNumberOfPlayersPerTable(int number)
    {
        if (number < 2)
        {
            throw new IllegalArgumentException("Can't allow less than 2 players per table: " + number);
        }
        if (number > 10)
        {
            throw new IllegalArgumentException("Can't allow more than 10 players as max per table: " + number);
        }
        tournamentPO.setMaxPlayerPerTable(number);
        placePlayersAtTables();
    }

    @Override
    public void placePlayersAtTables()
    {
        List<CompetitorBO> competitors = getActiveCompetitors();
        int numberOfTables = determineNumberTables(competitors.size());
        Collections.shuffle(competitors, new Random(System.nanoTime()));
        int position = 0;
        int table = 0;
        for(CompetitorBO competitorBO : competitors)
        {
            competitorBO.takeSeat(table, position);
            table++;
            if (table >= numberOfTables)
            {
                table = 0;
                position++;
            }
        }
    }

    private int determineNumberTables()
    {
        return determineNumberTables(getActiveCompetitorBOStream().count());
    }


    private int determineNumberTables(long competitorSize)
    {
        return new BigDecimal(competitorSize).divide(new BigDecimal(getMaximumNumberOfPlayersPerTable()), 0, RoundingMode.CEILING).intValue();
    }

    @Override
    public List<TableBO> getTables()
    {
        return new ArrayList<>(getInternalTables());
    }

    private List<ITableBO> getInternalTables()
    {
        List<ITableBO> result = new ArrayList<>();
        for(CompetitorBO competitor : getActiveCompetitors())
        {
            int tableNo = competitor.getTableNo();
            while(result.size() <= tableNo)
            {
                ITableBO table = factory.getTableBO(result.size());
                result.add(table);
            }
            result.get(tableNo).add(competitor);
        }
        return result;
    }

    @Override
    public List<RankBO> getRanks()
    {
        List<RankPO> ranks = new ArrayList<RankPO>(tournamentPO.getRanks());
        ranks.sort(RankPOComparators.DEFAULT);
        List<RankBO> result = new ArrayList<>(ranks.size());
        for (RankPO rank : ranks)
        {
            result.add(factory.getRankBO(rank, this));
        }
        return result;
    }

    @Override
    public RankBO createRank(int firstPosition, int lastPosition, BigDecimal percentage, BigDecimal amount)
    {
        RankPO rankPO = rankDao.createAndInsert(tournamentPO, firstPosition);
        rankPO.setPercentage(percentage);
        rankPO.setLastPosition(lastPosition);
        rankPO.setAmount(amount);
        validateAndFixRanks();
        return factory.getRankBO(rankPO, this);
    }

    @Override
    public void remove(RankBO rank)
    {
        Iterator<RankPO> ranks = tournamentPO.getRanks().iterator();
        while(ranks.hasNext())
        {
            RankPO rankPO = ranks.next();
            if (rankPO.getId().equals(rank.getID()))
            {
                rankDao.remove(rankPO);
                break;
            }
        }
        validateAndFixRanks();
    }

    private void validateAndFixRanks()
    {
        // fix first and last position
        List<RankPO> ranks = tournamentPO.getRanks();
        ranks.sort(RankPOComparators.DEFAULT);
        int firstPosition = 0;
        int lastPosition = 0;
        for (RankPO rank : ranks)
        {
            int nextFirstPosition = lastPosition + 1;
            firstPosition = rank.getFirstPosition();
            // fix first position
            if (firstPosition != nextFirstPosition)
            {
                rank.setFirstPosition(nextFirstPosition);
                firstPosition = nextFirstPosition;
            }
            lastPosition = rank.getLastPosition();
            if (lastPosition < firstPosition)
            {
                lastPosition = firstPosition;
                rank.setLastPosition(lastPosition);
            }
        }
        BigDecimal percentageSum = BigDecimal.ZERO;
        boolean setPercentageNull = false;
        for (RankPO rank : ranks)
        {
            if (setPercentageNull)
            {
                rank.setPercentage(null);
            }
            else
            {
                BigDecimal percentage = rank.getPercentage();
                if (percentage != null && !percentage.equals(BigDecimal.ZERO))
                {
                    percentageSum = percentageSum.add(percentage);
                    if (BigDecimal.ONE.subtract(percentageSum).signum() == -1)
                    {
                        rank.setPercentage(null);
                        setPercentageNull = true;
                    }
                }
            }
        }

    }

    @Override
    public int getInitialStacksize()
    {
        return tournamentPO.getInitialStacksize();
    }

    @Override
    public void setInitialStacksize(int initialStacksize)
    {
        tournamentPO.setInitialStacksize(initialStacksize);
    }

    @Override
    public int getFinalStacksize()
    {
        // TODO need to add re-buy here
        return tournamentPO.getInitialStacksize() * tournamentPO.getCompetitors().size();
    }

    @Override
    public void start()
    {
        tournamentPO.setCurrentRound(0);
    }

    @Override
    public CompetitorBO getCompetitorBO(String competitorID)
    {
        CompetitorPO competitorPO = competitorDao.getById(competitorID);
        if (competitorPO == null || !competitorPO.getTournamentPO().equals(tournamentPO))
        {
            return null;
        }
        return factory.getCompetitorBO(competitorPO, this);
    }

    @Override
    public List<CompetitorBO> getPlacedCompetitors()
    {
        return getCompetitorBOStream().filter(c -> CompetitorState.OUT.equals(c.getState())).collect(Collectors.toList());
    }

    @Override
    public Collection<CompetitorBO> levelOutTables()
    {
        int minTableNo = determineNumberTables();
        List<CompetitorBO> result = new ArrayList<>();
        while(minTableNo < getTables().size())
        {
            result.addAll(closeLastTable());
        }
        result.addAll(movePlayerFromLargeToSmallTable());
        return result;
    }

    private Collection<CompetitorBO> closeLastTable()
    {
        List<TableBO> tables = getTables();
        TableBO lastTable = tables.remove(tables.size() -1);
        List<CompetitorBO> result = lastTable.getCompetitors();
        int playerNo = 0;
        int maximumNumberOfPlayersPerTable = getMaximumNumberOfPlayersPerTable();
        while(playerNo < result.size())
        {
            for(TableBO table : tables)
            {
                if(table.getCompetitors().size() < maximumNumberOfPlayersPerTable)
                {
                    result.get(playerNo).takeSeat(table.getTableNo(), table.getCompetitors().size());
                    playerNo++;
                }
            }
            // refresh tables
            tables = getTables();
        }
        return result;
    }

    private Collection<CompetitorBO> movePlayerFromLargeToSmallTable()
    {
        long activePlayers = getActiveCompetitorBOStream().count(); // 26
        List<TableBO> tables = getTables();
        long average = activePlayers / tables.size(); // 3 tables -> 8
        boolean isEqual = average * tables.size() == activePlayers;
        long maxPlayers = isEqual ? average : average + 1;
        List<CompetitorBO> result = new ArrayList<>();
        // collect players more than one
        for(TableBO table : tables)
        {
            final List<CompetitorBO> competitors = new ArrayList<>(table.getCompetitors());
            while(competitors.size() > maxPlayers)
            {
                result.add(competitors.remove(competitors.size() - 1));
            }
        }
        int playerNo = 0;
        for(ITableBO table : getInternalTables())
        {
            while (table.getCompetitors().size() < average)
            {
                final CompetitorBO competitorBO = result.get(playerNo);
                competitorBO.takeSeat(table.getTableNo(), table.getCompetitors().size());
                table.add(competitorBO);
                playerNo++;
            }
        }
        if(playerNo < result.size())
        {
            for(ITableBO table : getInternalTables())
            {
                while (table.getCompetitors().size() < maxPlayers)
                {
                    final CompetitorBO competitorBO = result.get(playerNo);
                    competitorBO.takeSeat(table.getTableNo(), table.getCompetitors().size());
                    table.add(competitorBO);
                    playerNo++;
                }
            }
        }
        return result;
    }

}
