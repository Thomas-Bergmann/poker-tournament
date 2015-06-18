package de.hatoka.tournament.capi.business;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.math.BigDecimal;
import java.util.Currency;
import java.util.Date;
import java.util.List;

import javax.inject.Inject;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import de.hatoka.common.capi.business.Money;
import de.hatoka.tournament.internal.dao.DerbyEntityManagerRule;

public class TournamentBOTest
{
    private static final Date NOW = new Date();
    private static final String ACCOUNT_REF = TournamentBOTest.class.getSimpleName();
    private static final Money BUY_IN = Money.valueOf("5 EUR");
    @Rule
    public DerbyEntityManagerRule rule = new DerbyEntityManagerRule();

    @Inject
    private TournamentBusinessFactory factory;
    private TournamentBORepository tournamentBORepository;
    private TournamentBO underTest;

    @Before
    public void createTestObject()
    {
        TestBusinessInjectorProvider.get(rule.getModule()).injectMembers(this);
        tournamentBORepository = factory.getTournamentBORepository(ACCOUNT_REF);
        underTest = tournamentBORepository.createTournament("test", NOW);
    }

    @Test
    public void testAssignUnassign()
    {
        PlayerBO player1 = factory.getPlayerBORepository(ACCOUNT_REF).create("testPlayer1");
        CompetitorBO competitorBO = underTest.register(player1);
        assertTrue(underTest.getCompetitors().contains(competitorBO));
        underTest.unassign(competitorBO);
        assertFalse(underTest.getCompetitors().contains(competitorBO));
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
        PlayerBO player1 = factory.getPlayerBORepository(ACCOUNT_REF).create("testPlayer1");
        PlayerBO player2 = factory.getPlayerBORepository(ACCOUNT_REF).create("testPlayer2");
        CompetitorBO competitorBO_1 = underTest.register(player1);
        assertTrue("player one is competitor", underTest.isCompetitor(player1));
        assertFalse("player two is not competitor", underTest.isCompetitor(player2));
        CompetitorBO competitorBO_2 = underTest.register(player2);
        underTest.buyin(competitorBO_2);
        assertFalse("player one is not active", underTest.getActiveCompetitors().contains(competitorBO_1));
        assertTrue("player two is active", underTest.getActiveCompetitors().contains(competitorBO_2));
        assertEquals("correct player assigned", player1, competitorBO_1.getPlayer());
    }

    @Test
    public void testSimpleAttributes()
    {
        assertEquals(NOW, underTest.getStartTime());
        assertFalse("tournament.equals", underTest.equals(tournamentBORepository.createTournament("later", NOW)));
    }

    @Test
    public void testBlindLevels()
    {
        underTest.createBlindLevel(30, 100, 200, 0);
        underTest.createBlindLevel(30, 200, 400, 0);
        underTest.createPause(15);
        underTest.createBlindLevel(30, 500, 1000, 0);
        underTest.createBlindLevel(30, 500, 1000, 100);
        List<TournamentRoundBO> rounds = underTest.getBlindLevels();
        assertEquals("five rounds added", 5, rounds.size());
        assertNull("third round is pause", rounds.get(2).getBlindLevel());
        assertEquals("second round is level with small blind", Integer.valueOf(200), rounds.get(1).getBlindLevel().getSmallBlind());
        // remove pause
        underTest.remove(rounds.get(2));
        List<TournamentRoundBO> roundsAfterDelete = underTest.getBlindLevels();
        assertEquals("four rounds left", 4, roundsAfterDelete.size());
        for(TournamentRoundBO roundBO : roundsAfterDelete)
        {
            assertNotNull("only levels left", roundBO.getBlindLevel());
        }
    }

    @Test
    public void testRanks()
    {
        underTest.setBuyIn(BUY_IN);
        buyInPlayers(10);
        assertEquals(Money.valueOf("50 EUR"), underTest.getSumInplay());
        underTest.createRank(1, 1, new BigDecimal("0.5")); // (50-20) * 0.5 = 15
        underTest.createRank(2, 2, new BigDecimal("0.3")); // (50-20) * 0.5 = 15
        underTest.createRank(3, 3, new BigDecimal("0.2")); // means 25% = 7.5
        underTest.createFixRank(4, 9, BigDecimal.TEN);// means 25%  = 3.75
        List<RankBO> ranks = underTest.getRanks();
        assertEquals("five rounds added", 4, ranks.size());
        assertEquals("first rank", Money.valueOf(BigDecimal.valueOf(20), getCurrency()), ranks.get(0).getAmountPerPlayer());
        assertEquals("second rank", Money.valueOf(BigDecimal.valueOf(12), getCurrency()), ranks.get(1).getAmountPerPlayer());
        assertEquals("third rank", Money.valueOf(BigDecimal.valueOf(8), getCurrency()), ranks.get(2).getAmountPerPlayer());
        assertEquals("fourth rank", Money.valueOf(BigDecimal.valueOf(1.66), getCurrency()), ranks.get(3).getAmountPerPlayer());
    }

    private Currency getCurrency()
    {
        return underTest.getBuyIn().getCurrency();
    }

    private void buyInPlayers(Integer numberOfPlayers)
    {
        for(int i=1; i<=numberOfPlayers;i++)
        {
            PlayerBO player = factory.getPlayerBORepository(ACCOUNT_REF).create("testPlayer_iterator_" + i);
            CompetitorBO competitorBO = underTest.register(player);
            underTest.buyin(competitorBO);
        }
    }
}
