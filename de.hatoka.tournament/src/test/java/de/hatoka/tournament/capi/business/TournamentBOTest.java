package de.hatoka.tournament.capi.business;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Currency;
import java.util.Date;
import java.util.List;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import de.hatoka.common.capi.locale.CountryHelper;
import de.hatoka.common.capi.locale.LocalizationConstants;
import de.hatoka.common.capi.math.Money;
import de.hatoka.player.capi.business.PlayerBO;
import de.hatoka.player.capi.business.PlayerBORepository;
import de.hatoka.player.capi.business.PlayerRef;
import de.hatoka.user.capi.business.UserRef;
import tests.de.hatoka.tournament.TournamentTestConfiguration;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = { TournamentTestConfiguration.class })
public class TournamentBOTest
{
    private static final Date START_DATE = parseDate("2011-11-25T08:50");
    private static final Date LATER_DATE = parseDate("2011-11-28T08:45");

    private static final UserRef USER_REF = UserRef.valueOfLocal(TournamentBOTest.class.getSimpleName());
    private static final Money BUY_IN = Money.valueOf("5 EUR");

    @Autowired
    private TournamentBORepository tournamentBORepository;
    @Autowired
    private PlayerBORepository playerBORepository;

    private TournamentBO underTest;
    private List<PlayerBO> createdPlayer = new ArrayList<>();

    @BeforeEach
    public void createTestObject()
    {
        underTest = tournamentBORepository.createTournament(USER_REF, "test", START_DATE, "test");
    }

    @AfterEach
    public void destroyCreatedObjects()
    {
        if (underTest != null)
        {
            underTest.remove();
        }
        createdPlayer.forEach(PlayerBO::remove);
        createdPlayer.clear();
    }

    @Test
    public void testAssignUnassign()
    {
        PlayerBO player1 = createPlayer("test-unassigned");
        CompetitorBO competitorBO = underTest.register(player1);
        assertTrue(underTest.getCompetitors().contains(competitorBO));
        underTest.unassign(competitorBO);
        assertFalse(underTest.getCompetitors().contains(competitorBO));
    }

    private PlayerBO createPlayer(String name)
    {
        PlayerRef playerRef = PlayerRef.valueOf(USER_REF, name);
        PlayerBO result = playerBORepository.createPlayer(playerRef, name + "-nick");
        createdPlayer.add(result);
        return result;
    }

    @Test
    public void testSumInPlay()
    {
        underTest.setBuyIn(BUY_IN);
        buyInPlayers(2);
        assertEquals(Money.valueOf("10 EUR"), underTest.getSumInplay());
    }

    @Test
    public void testCompetitors()
    {
        PlayerBO player1 = createPlayer("testPlayer1");
        PlayerBO player2 = createPlayer("testPlayer2");
        CompetitorBO competitorBO_1 = underTest.register(player1);
        assertTrue(underTest.isCompetitor(player1), "player one is competitor");
        assertFalse(underTest.isCompetitor(player2), "player two is not competitor");
        CompetitorBO competitorBO_2 = underTest.register(player2);
        competitorBO_2.buyin();
        assertFalse(underTest.getActiveCompetitors().contains(competitorBO_1), "player one is not active");
        assertTrue(underTest.getActiveCompetitors().contains(competitorBO_2), "player two is active");
        assertEquals(player1, competitorBO_1.getPlayer());
        competitorBO_1.buyin();
        assertTrue(underTest.getActiveCompetitors().contains(competitorBO_1), "player one is now active");
    }

    @Test
    public void testSimpleAttributes()
    {
        assertEquals(START_DATE, underTest.getStartTime());
        assertFalse(underTest.equals(tournamentBORepository.createTournament(USER_REF, "later", LATER_DATE, "later")), "tournament.equals");
    }

    @Test
    public void testBlindLevels()
    {
        BlindLevelBO level1 = underTest.createBlindLevel(30, 100, 200, 0);
        BlindLevelBO level2 = underTest.createBlindLevel(30, 200, 400, 0);
        underTest.createPause(15);
        underTest.createBlindLevel(30, 500, 1000, 0);
        underTest.createBlindLevel(30, 500, 1000, 100);
        List<TournamentRoundBO> rounds = underTest.getTournamentRounds();
        assertEquals(5, rounds.size());
        assertNull(rounds.get(2).getBlindLevel(), "third round is pause");
        assertEquals(Integer.valueOf(200), rounds.get(1).getBlindLevel().getSmallBlind(), "second round is level with small blind");
        // remove pause
        underTest.remove(rounds.get(2));
        List<TournamentRoundBO> roundsAfterDelete = underTest.getTournamentRounds();
        assertEquals(4, roundsAfterDelete.size(), "four rounds left");
        for (TournamentRoundBO roundBO : roundsAfterDelete)
        {
            assertNotNull(roundBO.getBlindLevel(), "only levels left");
        }
        level1.start();
        assertTrue(level1.isActive());
        // is now assertEquals(NOW, level1.getStartTime());
        level2.start();
        assertFalse(level1.isActive());
        assertTrue(level2.isActive());
    }

    @Test
    public void testCurrentAndNextBlindLevels()
    {
        BlindLevelBO level1 = underTest.createBlindLevel(30, 100, 200, 0);
        BlindLevelBO level2 = underTest.createBlindLevel(30, 200, 400, 0);
        PauseBO pause = underTest.createPause(15);
        BlindLevelBO level3 = underTest.createBlindLevel(30, 500, 1000, 0);
        BlindLevelBO level4 = underTest.createBlindLevel(30, 500, 1000, 100);
        assertEquals(level1, underTest.getCurrentBlindLevel(), "current level in case tournament not started");
        assertEquals(level2, underTest.getNextBlindLevel(), "next level in case tournament not started");
        level1.start();
        assertEquals(level1, underTest.getCurrentBlindLevel(), "current level (1 started)");
        assertEquals(level2, underTest.getNextBlindLevel(), "next level (1 started)");
        level2.start();
        assertEquals(level2, underTest.getCurrentBlindLevel(), "current level (2 started)");
        assertNull(underTest.getNextBlindLevel(), "next level (2 started)");
        pause.start();
        assertNull(underTest.getCurrentBlindLevel(), "current level (pause)");
        assertEquals(level3,underTest.getNextBlindLevel(), "next level (pause");
        level3.start();
        assertEquals(level3,underTest.getCurrentBlindLevel(), "current level (3 started)");
        assertEquals(level4,underTest.getNextBlindLevel(), "next level (3 started)");
        level4.start();
        assertEquals(level4,underTest.getCurrentBlindLevel(), "current level (4 started)");
        assertNull(underTest.getNextBlindLevel(), "last level so next is null");
    }

    @Test
    public void testPlayerLifeCycle()
    {
        PlayerBO player1 = createPlayer("testPlayer1");
        CompetitorBO competitor = underTest.register(player1);
        underTest.setReBuy(BigDecimal.TEN);
        BlindLevelBO blindLevel = underTest.createBlindLevel(30, 100, 200, 0);
        blindLevel.allowRebuy(true);
        competitor.buyin();
        underTest.start();
        Money rebuy = underTest.getCurrentRebuy();
        assertNotNull(rebuy);
        competitor.rebuy();
        assertEquals(rebuy.add(underTest.getBuyIn()), competitor.getInPlay(), "player one payed buyin and rebuy");
        underTest.seatOpen(competitor);
        assertFalse(competitor.isActive(), "player is not longer active");
    }

    @Test
    public void testRanks()
    {
        underTest.setBuyIn(BUY_IN);
        buyInPlayers(10);
        assertEquals(Money.valueOf("50 EUR"), underTest.getSumInplay());
        underTest.createRank(1, 1, new BigDecimal("0.5")); // (50-10) * 0.5 = 20
        underTest.createRank(2, 2, new BigDecimal("0.3")); // (50-10) * 0.3 = 12
        underTest.createRank(3, 3, new BigDecimal("0.2")); // (50-10) * 0.2 = 8
        underTest.createFixRank(4, 9, BigDecimal.TEN);// means 10 / 6 = 1.66
        List<RankBO> ranks = underTest.getRanks();
        assertEquals(4, ranks.size());
        underTest.start();

        assertEquals(Money.valueOf(BigDecimal.valueOf(20), getCurrency()), ranks.get(0).getAmountPerPlayer());
        assertEquals(Money.valueOf(BigDecimal.valueOf(12), getCurrency()), ranks.get(1).getAmountPerPlayer());
        assertEquals(Money.valueOf(BigDecimal.valueOf(8), getCurrency()), ranks.get(2).getAmountPerPlayer());
        assertEquals(Money.valueOf(BigDecimal.valueOf(1.66), getCurrency()), ranks.get(3).getAmountPerPlayer());
    }

    private Currency getCurrency()
    {
        return underTest.getBuyIn().getCurrency();
    }

    private void buyInPlayers(Integer numberOfPlayers)
    {
        for (int i = 1; i <= numberOfPlayers; i++)
        {
            PlayerBO player = createPlayer("testPlayer_iterator_" + i);
            CompetitorBO competitorBO = underTest.register(player);
            competitorBO.buyin();
        }
    }

    @Test
    public void testPlaceCompetitors()
    {
        underTest.setMaximumNumberOfPlayersPerTable(10);
        for (int i = 0; i < 17; i++)
        {
            CompetitorBO competitor = createCompetitor("player testPlaceCompetitors" + i);
            competitor.buyin();
        }
        createCompetitor("inactive player testPlaceCompetitors");
        underTest.placePlayersAtTables();

        // test tables
        Collection<TableBO> tables = underTest.getTables();
        assertEquals(2, tables.size());

        // test active players
        assertEquals(9, maxPlayersOnTable(tables));
        assertEquals(17, sumPlayersOnTable(tables));

        // remove one player (now 16 - 8 on each table)
        assertTrue(underTest.getPlacedCompetitors().isEmpty(), "no placed players at start");
        removeFirstPlayer(tables);
        assertEquals(1, underTest.getPlacedCompetitors().size());
        assertEquals(17, underTest.getPlacedCompetitors().get(0).getPosition().intValue());

        tables = underTest.getTables();
        assertEquals(8, maxPlayersOnTable(tables));
        assertEquals(16, sumPlayersOnTable(tables));

        assertTrue(underTest.levelOutTables().isEmpty(), "no movedPlayer");

        // remove two player (now 14 - 6 on first and 8 on second table)
        removeFirstPlayer(tables);
        removeFirstPlayer(tables);
        tables = underTest.getTables();
        assertEquals(8, maxPlayersOnTable(tables));
        assertEquals(14, sumPlayersOnTable(tables));
        Collection<CompetitorBO> movedCompetitors = underTest.levelOutTables();
        assertEquals(1, movedCompetitors.size());
        tables = underTest.getTables();
        assertEquals(7, maxPlayersOnTable(tables));
        assertEquals(14, sumPlayersOnTable(tables));
    }

    @Test
    public void testRemoveLastTable()
    {
        // 9 players at 4er tables -> 3 players at 3 tables ->
        underTest.setMaximumNumberOfPlayersPerTable(4);
        for (int i = 0; i < 9; i++)
        {
            createCompetitor("player " + i).buyin();
        }
        underTest.placePlayersAtTables();
        // remove second of second table
        List<TableBO> tables = underTest.getTables();
        assertEquals(3, tables.size());
        underTest.seatOpen(tables.get(1).getCompetitors().get(1));

        // than 4 players at 2 tables and 3 moved players
        Collection<CompetitorBO> movedCompetitors = underTest.levelOutTables();
        assertEquals(3, movedCompetitors.size(), "3 from last table moved");
    }

    private void removeFirstPlayer(Collection<TableBO> tables)
    {
        CompetitorBO firstCompetitor = tables.iterator().next().getCompetitors().stream().filter(c -> c.isActive())
                        .findAny().get();
        underTest.seatOpen(firstCompetitor);
    }

    private static int maxPlayersOnTable(Collection<TableBO> tables)
    {
        int maxPlayer = 0;
        for (TableBO table : tables)
        {
            final int playersOnTable = table.getCompetitors().size();
            if (maxPlayer < playersOnTable)
            {
                maxPlayer = playersOnTable;
            }
        }
        return maxPlayer;
    }

    private static int sumPlayersOnTable(Collection<TableBO> tables)
    {
        int sumPlayers = 0;
        for (TableBO table : tables)
        {
            final int playersOnTable = table.getCompetitors().size();
            sumPlayers += playersOnTable;
        }
        return sumPlayers;
    }

    private CompetitorBO createCompetitor(String name)
    {
        return underTest.register(createPlayer(name));
    }

    private static Date parseDate(String dateString)
    {
        SimpleDateFormat result = new SimpleDateFormat(LocalizationConstants.XML_DATEFORMAT_MINUTES);
        result.setTimeZone(CountryHelper.TZ_BERLIN);
        try
        {
            return result.parse(dateString);
        }
        catch(ParseException e)
        {
            throw new RuntimeException(e);
        }
    }
}
