package de.hatoka.tournament.internal.business;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import jakarta.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import de.hatoka.common.capi.configuration.DateProvider;
import de.hatoka.common.capi.math.Money;
import de.hatoka.common.capi.persistence.MoneyPO;
import de.hatoka.common.capi.persistence.MoneyPOConverter;
import de.hatoka.group.capi.business.GroupRef;
import de.hatoka.player.capi.business.HistoryBORepository;
import de.hatoka.player.capi.business.HistoryEntryBO;
import de.hatoka.player.capi.business.PlayerBO;
import de.hatoka.tournament.capi.business.BlindLevelBO;
import de.hatoka.tournament.capi.business.CompetitorBO;
import de.hatoka.tournament.capi.business.PauseBO;
import de.hatoka.tournament.capi.business.RankBO;
import de.hatoka.tournament.capi.business.TableBO;
import de.hatoka.tournament.capi.business.TournamentComparators;
import de.hatoka.tournament.capi.business.TournamentRef;
import de.hatoka.tournament.capi.business.TournamentRoundBO;
import de.hatoka.tournament.capi.types.CompetitorState;
import de.hatoka.tournament.internal.persistence.BlindLevelDao;
import de.hatoka.tournament.internal.persistence.BlindLevelPO;
import de.hatoka.tournament.internal.persistence.CompetitorDao;
import de.hatoka.tournament.internal.persistence.CompetitorPO;
import de.hatoka.tournament.internal.persistence.RankDao;
import de.hatoka.tournament.internal.persistence.RankPO;
import de.hatoka.tournament.internal.persistence.TournamentDao;
import de.hatoka.tournament.internal.persistence.TournamentPO;
import de.hatoka.user.capi.business.UserRef;

@Component
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class TournamentBOImpl implements ITournamentBO
{
    @Autowired
    private TournamentDao tournamentDao;
    @Autowired
    private CompetitorDao competitorDao;
    @Autowired
    private BlindLevelDao blindLevelDao;
    @Autowired
    private RankDao rankDao;
    @Autowired
    private CompetitorBOFactory competitorFactory;
    @Autowired
    private BlindLevelBOFactory blindLevelFactory;
    @Autowired
    private PauseBOFactory pauseFactory;
    @Autowired
    private RankBOFactory rankFactory;
    @Autowired
    private HistoryBORepository historyRepository;
    @Autowired
    private DateProvider dateProvider;

    private TournamentPO tournamentPO;
    private Long tournament_id;
    // required to return same BOs on multiple getRanks() calls, otherwise the PO is not updated inside the old returned BO.
    private List<IRankBO> rankBOs = null;

    public TournamentBOImpl(TournamentPO tournamentPO)
    {
        this.tournamentPO = tournamentPO;
        this.tournament_id = tournamentPO.getInternalID();
    }

    @Override
    public CompetitorBO register(PlayerBO playerBO)
    {
        CompetitorPO po = new CompetitorPO();
        po.setPlayerRef(playerBO.getRef().getGlobalRef());
        po.setTournamentID(tournament_id);
        return getBO(competitorDao.saveAndFlush(po));
    }

    @Override
    public Money getCurrentRebuy()
    {
        final List<TournamentRoundBO> blindLevels = getTournamentRounds();
        int currentRound = getPO().getCurrentRound();
        if (currentRound < 0 || blindLevels.size() <= currentRound)
        {
            return null;
        }
        if (blindLevels.get(currentRound).isRebuyAllowed())
        {
            return valueOf(getPO().getReBuy());
        }
        return null;
    }

    private Money valueOf(MoneyPO po)
    {
        return MoneyPOConverter.valueOf(po);
    }

    @Override
    public void seatOpen(CompetitorBO competitorBO)
    {
        if (!competitorBO.isActive() || !(competitorBO instanceof ICompetitorBO))
        {
            throw new IllegalStateException("seatOpen not allowed at inactive competitors");
        }
        int position = getActiveCompetitors().size();
        Money result = getResultForPosition(position);
        ICompetitorBO iTournamentCompetitor = (ICompetitorBO)competitorBO;
        iTournamentCompetitor.seatOpen(result, position);
    }

    private Money getResultForPosition(int position)
    {
        List<RankBO> assignedRanks = getRanks().stream()
                        .filter(rankBO -> rankBO.getFirstPosition() <= position && position <= rankBO.getLastPosition())
                        .collect(Collectors.toList());
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

    private Stream<ICompetitorBO> getActiveCompetitorBOStream()
    {
        return getCompetitorBOStream().filter(competitor -> competitor.isActive());
    }

    private ICompetitorBO getBO(CompetitorPO competitorPO)
    {
        return competitorFactory.get(competitorPO, this);
    }

    @Override
    public Money getBuyIn()
    {
        return valueOf(getPO().getBuyIn());
    }

    @Override
    public List<CompetitorBO> getCompetitors()
    {
        return getCompetitorBOStream().sorted(TournamentComparators.COMPETITOR).collect(Collectors.toList());
    }

    private Stream<ICompetitorBO> getCompetitorBOStream()
    {
        return getCompetitorPOs().stream().map(this::getBO);
    }

    private List<CompetitorPO> getCompetitorPOs()
    {
        return competitorDao.getByTournamentID(tournament_id);
    }

    @Override
    public Date getStartTime()
    {
        return getPO().getStartDate();
    }

    @Override
    public String getName()
    {
        return getPO().getName();
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
        String playerRef = player.getRef().getGlobalRef();
        return getCompetitorPOs().stream()
                        .anyMatch(competitorPO -> playerRef.equals(competitorPO.getPlayerRef()));
    }

    @Override
    @Transactional
    public void remove()
    {
        historyRepository.deleteEntries(getRef().getGlobalRef());
        blindLevelDao.deleteAllInBatch(blindLevelDao.getByTournamentID(tournament_id));
        rankDao.deleteAllInBatch(rankDao.getByTournamentID(tournament_id));
        competitorDao.deleteAllInBatch(competitorDao.getByTournamentID(tournament_id));
        tournamentDao.deleteById(tournament_id);
        tournament_id = null;
        tournamentPO = null;
    }

    @Override
    public void setBuyIn(Money buyIn)
    {
        getPO().setBuyIn(MoneyPOConverter.persistence(buyIn));
        savePO();
    }

    @Override
    public void unassign(CompetitorBO competitorBO)
    {
        if (competitorBO.isActive() || !competitorBO.getResult().isZero())
        {
            throw new IllegalStateException("Can't remove competitor, is/was in play with result.");
        }
        competitorBO.remove();
    }

    @Override
    public int hashCode()
    {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getPO() == null) ? 0 : getPO().hashCode());
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
        if (getPO() == null)
        {
            if (other.tournamentPO != null)
                return false;
        }
        else if (!getPO().equals(other.tournamentPO))
            return false;
        return true;
    }

    /**
     * Sort competitors recalculates the position of competitors.
     */
    @Override
    public void sortCompetitors()
    {
        // TODO sort by current stack or price or name
    }

    @Override
    public List<HistoryEntryBO> getHistoryEntries()
    {
        return historyRepository.getEntries(getRef().getGlobalRef());
    }

    @Override
    public void setStartTime(Date date)
    {
        getPO().setStartDate(date);
        savePO();
    }

    @Override
    public BlindLevelBO createBlindLevel(int duration, int smallBlind, int bigBlind, int ante)
    {
        int nextLevel = getNextBlindLevelPositon();
        BlindLevelPO blindLevelPO = new BlindLevelPO();
        blindLevelPO.setTournamentID(tournament_id);
        blindLevelPO.setDuration(duration);
        blindLevelPO.setPause(false);
        blindLevelPO.setSmallBlind(smallBlind);
        blindLevelPO.setBigBlind(bigBlind);
        blindLevelPO.setAnte(ante);
        blindLevelPO.setPosition(nextLevel);
        blindLevelPO = blindLevelDao.saveAndFlush(blindLevelPO);
        return blindLevelFactory.get(blindLevelPO, this);
    }

    private int getNextBlindLevelPositon()
    {
        int result = -1;
        for (BlindLevelPO bl : getBlindLevelPOs())
        {
            if (bl.getPosition() > result)
            {
                result = bl.getPosition();
            }
        }
        return result + 1;
    }

    @Override
    public PauseBO createPause(int duration)
    {
        int nextLevel = getNextBlindLevelPositon();
        BlindLevelPO blindLevelPO = new BlindLevelPO();
        blindLevelPO.setTournamentID(tournament_id);
        blindLevelPO.setDuration(duration);
        blindLevelPO.setPause(true);
        blindLevelPO.setPosition(nextLevel);
        blindLevelPO = blindLevelDao.saveAndFlush(blindLevelPO);
        return pauseFactory.get(blindLevelPO, this);
    }

    @Override
    public void setName(String name)
    {
        getPO().setName(name);
        savePO();
    }

    @Override
    public List<TournamentRoundBO> getTournamentRounds()
    {
        List<BlindLevelPO> blindLevels = getBlindLevelPOs();
        List<TournamentRoundBO> result = new ArrayList<TournamentRoundBO>(blindLevels.size());
        for (BlindLevelPO blindLevelPO : blindLevels)
        {
            if (blindLevelPO.isPause())
            {
                result.add(pauseFactory.get(blindLevelPO, this));
            }
            else
            {
                result.add(blindLevelFactory.get(blindLevelPO, this));
            }
        }
        return result;
    }

    @Override
    public int getMaximumNumberOfPlayersPerTable()
    {
        return getPO().getMaxPlayerPerTable();
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
        getPO().setMaxPlayerPerTable(number);
        savePO();
        placePlayersAtTables();
    }

    @Override
    public void placePlayersAtTables()
    {
        List<ICompetitorBO> competitors = getActiveCompetitorBOStream().collect(Collectors.toList());
        int numberOfTables = determineNumberTables(competitors.size());
        Collections.shuffle(competitors, new Random(System.nanoTime()));
        int position = 0;
        int table = 0;
        for (ICompetitorBO competitorBO : competitors)
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
        return new BigDecimal(competitorSize)
                        .divide(new BigDecimal(getMaximumNumberOfPlayersPerTable()), 0, RoundingMode.CEILING)
                        .intValue();
    }

    @Override
    public List<TableBO> getTables()
    {
        return new ArrayList<>(getInternalTables());
    }

    private List<ITableBO> getInternalTables()
    {
        List<ITableBO> result = new ArrayList<>();
        for (CompetitorBO competitor : getActiveCompetitorBOStream().filter(c -> c.getTableNo() != null)
                        .collect(Collectors.toList()))
        {
            int tableNo = competitor.getTableNo();
            while(result.size() <= tableNo)
            {
                ITableBO table = getTable(result.size());
                result.add(table);
            }
            result.get(tableNo).add(competitor);
        }
        return result;
    }

    private ITableBO getTable(int number)
    {
        return new TableBOImpl(number);
    }

    @Override
    public List<RankBO> getRanks()
    {
        return getIRanks().stream().map(r -> (RankBO) r).collect(Collectors.toList());
    }

    private List<IRankBO> getIRanks()
    {
        if (rankBOs == null)
        {
            List<RankPO> ranks = new ArrayList<RankPO>(getRankPOs());
            ranks.sort(RankPOComparators.DEFAULT);
            List<IRankBO> result = new ArrayList<>(ranks.size());
            for (RankPO rank : ranks)
            {
                result.add(rankFactory.get(rank, this));
            }
            rankBOs = result;
        }
        return rankBOs;
    }

    private List<RankPO> getRankPOs()
    {
        return rankDao.getByTournamentID(tournament_id);
    }

    @Override
    public RankBO createRank(int firstPosition, int lastPosition, BigDecimal percentage, BigDecimal amount)
    {
        List<IRankBO> existingRanks = getIRanks();
        RankPO rankPO = new RankPO();
        rankPO.setTournamentID(tournament_id);
        rankPO.setFirstPosition(firstPosition);
        rankPO.setPercentage(percentage);
        rankPO.setLastPosition(lastPosition);
        rankPO.setAmount(amount);
        rankDao.saveAndFlush(rankPO);
        validateAndFixRanks();
        IRankBO result = rankFactory.get(rankPO, this);
        existingRanks.add(result);
        return result;
    }

    @Override
    public void removeRank(IRankBO rank)
    {
        rankBOs.remove(rank);
    }

    private void validateAndFixRanks()
    {
        // fix first and last position
        List<RankPO> ranks = getRankPOs();
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
                rank = rankDao.save(rank);
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
                        rank = rankDao.save(rank);
                    }
                }
            }
        }

    }

    @Override
    public int getInitialStacksize()
    {
        return getPO().getInitialStacksize();
    }

    @Override
    public void setInitialStacksize(int initialStacksize)
    {
        getPO().setInitialStacksize(initialStacksize);
    }

    @Override
    public int getFinalStacksize()
    {
        return getPO().getInitialStacksize() * getCompetitorPOs().size();
    }

    @Override
    public void start()
    {
        getPO().setStartDate(dateProvider.get());
        defineBlindLevelStartTimes();
        getPO().setCurrentRound(0);
        savePO();
        defineMoneyAtRanks();
    }

    @Override
    public List<CompetitorBO> getPlacedCompetitors()
    {
        return getCompetitorBOStream().filter(c -> CompetitorState.OUT.equals(c.getState()))
                        .collect(Collectors.toList());
    }

    @Override
    public Collection<CompetitorBO> levelOutTables()
    {
        int minTableNo = determineNumberTables();
        List<CompetitorBO> result = new ArrayList<>();
        if (minTableNo == 0)
        {
            return Collections.emptyList();
        }
        while(minTableNo < getTables().size())
        {
            result.addAll(closeLastTable());
        }
        result.addAll(movePlayerFromLargeToSmallTable());
        return result;
    }

    private Collection<CompetitorBO> closeLastTable()
    {
        List<ITableBO> tables = getInternalTables();
        TableBO lastTable = tables.remove(tables.size() - 1);
        List<CompetitorBO> result = lastTable.getCompetitors();
        int playerNo = 0;
        int maximumNumberOfPlayersPerTable = getMaximumNumberOfPlayersPerTable();
        while(playerNo < result.size())
        {
            for (ITableBO table : tables)
            {
                if (table.getCompetitors().size() < maximumNumberOfPlayersPerTable)
                {
                    placeCompetitorAtFreePlace(result.get(playerNo), table);
                    playerNo++;
                    break;
                }
            }
        }
        return result;
    }

    private void placeCompetitorAtFreePlace(CompetitorBO competitorBO, ITableBO table)
    {
        HashSet<Integer> seats = new HashSet<>(getMaximumNumberOfPlayersPerTable());
        for (int i = 0; i < getMaximumNumberOfPlayersPerTable(); i++)
        {
            seats.add(Integer.valueOf(i));
        }
        for (CompetitorBO seat : table.getCompetitors())
        {
            seats.remove(seat.getSeatNo());
        }
        if (competitorBO instanceof ICompetitorBO)
        {
            ((ICompetitorBO) competitorBO).takeSeat(table.getTableNo(), seats.iterator().next());
            table.add(competitorBO);
        }
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
        for (TableBO table : tables)
        {
            final List<CompetitorBO> competitors = new ArrayList<>(table.getCompetitors());
            while(competitors.size() > maxPlayers)
            {
                result.add(competitors.remove(competitors.size() - 1));
            }
        }
        if (!result.isEmpty())
        {
            int playerNo = 0;
            for (ITableBO table : getInternalTables())
            {
                while(table.getCompetitors().size() < average)
                {
                    placeCompetitorAtFreePlace(result.get(playerNo), table);
                    playerNo++;
                }
            }
            if (playerNo < result.size())
            {
                for (ITableBO table : getInternalTables())
                {
                    while(table.getCompetitors().size() < maxPlayers)
                    {
                        placeCompetitorAtFreePlace(result.get(playerNo), table);
                        playerNo++;
                    }
                }
            }
        }
        return result;
    }

    @Override
    public void setReBuy(BigDecimal rebuy)
    {
        getPO().setReBuy(new MoneyPO(rebuy, getBuyIn().getCurrency().getCurrencyCode()));
        savePO();
    }

    @Override
    public Money getReBuy()
    {
        return MoneyPOConverter.valueOf(getPO().getReBuy());
    }

    @Override
    public BlindLevelBO getCurrentBlindLevel()
    {
        final List<TournamentRoundBO> blindLevels = getTournamentRounds();
        TournamentRoundBO currentRound = blindLevels
                        .get(getPO().getCurrentRound() == -1 ? 0 : getPO().getCurrentRound());
        if (currentRound instanceof BlindLevelBO)
        {
            return (BlindLevelBO)currentRound;
        }
        return null;
    }

    @Override
    public BlindLevelBO getNextBlindLevel()
    {
        final List<TournamentRoundBO> blindLevels = getTournamentRounds();
        if (blindLevels.size() < 2)
        {
            return null;
        }
        int currentRoundIdx = getPO().getCurrentRound();
        if (currentRoundIdx == -1)
        {
            currentRoundIdx = 0;
        }
        currentRoundIdx++; // next level requested
        if (currentRoundIdx >= blindLevels.size())
        {
            return null;
        }
        TournamentRoundBO nextRound = blindLevels.get(currentRoundIdx);
        if (nextRound instanceof BlindLevelBO)
        {
            return (BlindLevelBO)nextRound;
        }
        return null;
    }

    @Override
    public PauseBO getNextPause()
    {
        int currentRoundIdx = getPO().getCurrentRound();
        if (currentRoundIdx == -1)
        {
            currentRoundIdx = 0;
        }
        final List<TournamentRoundBO> blindLevels = getTournamentRounds();
        TournamentRoundBO nextRound = blindLevels.get(currentRoundIdx++);
        while(nextRound != null && !(nextRound instanceof PauseBO))
        {
            nextRound = blindLevels.get(currentRoundIdx++);
        }
        return (PauseBO)nextRound;
    }

    @Override
    public void defineBlindLevelStartTimes()
    {
        Date nextStart = getPO().getStartDate();
        if (nextStart == null)
        {
            nextStart = dateProvider.get();
        }
        int currentRound = 0;
        List<BlindLevelPO> blindLevels = getBlindLevelPOs();
        for (currentRound = 0; currentRound < blindLevels.size(); currentRound++)
        {
            BlindLevelPO blindLevel = blindLevels.get(currentRound);
            final Date levelStartDate = blindLevel.getStartDate();
            if (currentRound <= getPO().getCurrentRound() && levelStartDate != null)
            {
                nextStart = levelStartDate;
            }
            else
            {
                blindLevel.setStartDate(nextStart);
            }
            nextStart = new Date(nextStart.getTime() + (blindLevel.getDuration() * 60_000));
        }
    }

    private List<BlindLevelPO> getBlindLevelPOs()
    {
        List<BlindLevelPO> blindLevels = blindLevelDao.getByTournamentID(tournament_id);
        blindLevels.sort((o1, o2) -> o1.getPosition().compareTo(o2.getPosition()));
        return blindLevels;
    }

    @Override
    public void start(IPauseBO pause)
    {
        getPO().setCurrentRound(pause.getPosition());
        savePO();
        pause.setStartDate(dateProvider.get());
        defineBlindLevelStartTimes();
    }

    @Override
    public Integer getCurrentRound()
    {
        return getPO().getCurrentRound();
    }

    @Override
    public void start(IBlindLevelBO blindLevel)
    {
        getPO().setCurrentRound(blindLevel.getPosition());
        blindLevel.setStartDate(dateProvider.get());
        defineBlindLevelStartTimes();
    }

    @Override
    public TournamentRef getRef()
    {
        return TournamentRef.valueOf(UserRef.valueOfLocal(getPO().getOwnerRef()), getPO().getLocalRef());
    }

    @Override
    public GroupRef getGroupRef()
    {
        return GroupRef.valueOfGlobal(getPO().getGroupRef());
    }

    @Override
    public void setGroupRef(GroupRef groupRef)
    {
        getPO().setGroupRef(groupRef.getGlobalRef());
        savePO();
    }

    @Override
    public void remove(TournamentRoundBO tournamentRoundBO)
    {
        Integer pos = tournamentRoundBO.getPosition();
        Optional<BlindLevelPO> levelPO = getBlindLevelPOs().stream().filter(l -> pos.equals(l.getPosition())).findAny();
        if (levelPO.isPresent())
        {
            blindLevelDao.delete(levelPO.get());
        }
    }

    private TournamentPO getPO()
    {
        if (tournamentPO == null)
        {
            Optional<TournamentPO> opt = tournamentDao.findById(tournament_id);
            if (!opt.isPresent())
            {
                throw new IllegalStateException("tournament with id '"+ tournament_id + "' not longer available.");
            }
            tournamentPO = opt.get();
        }
        return tournamentPO;
    }

    private void savePO()
    {
        tournamentPO = tournamentDao.save(getPO());
    }

    private void defineMoneyAtRanks()
    {
        BigDecimal sumPerc = BigDecimal.ZERO;
        List<RankBO> ranksWithPercentage = getRanks().stream().filter(r -> !BigDecimal.ZERO.equals(r.getPercentage().stripTrailingZeros())).collect(Collectors.toList());
        if (ranksWithPercentage.isEmpty())
        {
            return;
        }
        for(RankBO rank : ranksWithPercentage)
        {
            sumPerc = sumPerc.add(rank.getPercentage());
        }
        if (!BigDecimal.ONE.equals(sumPerc.stripTrailingZeros()))
        {
            throw new IllegalStateException("Sum of percentage for all ranks needs to be 1, but is " + sumPerc);
        }
        Money sum = getSumInplay();
        Money amountForDistribution = sum.subtract(getRankFixAmounts());
        for(IRankBO rank : getIRanks())
        {
            Money fixAmount = rank.getAmount();
            if (null != fixAmount)
            {
                continue;
            }
            BigDecimal perc = rank.getPercentage();
            if (null == perc || BigDecimal.ZERO.equals(perc))
            {
                rank.setAmount(Money.NOTHING);
            }
            else
            {
                // calc and set amount
                rank.setAmount(amountForDistribution.multiply(perc));
            }
        }
    }

    private Money getRankFixAmounts()
    {
        Money result = Money.NOTHING;
        for(RankBO rank : getRanks())
        {
            // collect percentage related amount
            if (BigDecimal.ZERO.equals(rank.getPercentage().stripTrailingZeros()))
            {
                result = result.add(rank.getAmount());
            }
        }
        return result;
    }
}
