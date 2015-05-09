package de.hatoka.tournament.capi.business;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

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
    private static final Money BUY_IN = Money.getInstance("5 EUR");
    @Rule
    public DerbyEntityManagerRule rule = new DerbyEntityManagerRule();

    @Inject
    private TournamentBusinessFactory factory;
    private TournamentBORepository tournamentBORepository;
    private TournamentBO underTest;
    private PlayerBO player1;
    private PlayerBO player2;

    @Before
    public void createTestObject()
    {
        TestBusinessInjectorProvider.get(rule.getModule()).injectMembers(this);
        tournamentBORepository = factory.getTournamentBORepository(ACCOUNT_REF);
        underTest = tournamentBORepository.createTournament("test", NOW);
        player1 = factory.getPlayerBORepository(ACCOUNT_REF).create("testPlayer1");
        player2 = factory.getPlayerBORepository(ACCOUNT_REF).create("testPlayer2");
    }

    @Test
    public void testAssignUnassign()
    {
        CompetitorBO competitorBO = underTest.register(player1);
        assertTrue(underTest.getCompetitors().contains(competitorBO));
        underTest.unassign(competitorBO);
        assertFalse(underTest.getCompetitors().contains(competitorBO));
    }

    @Test
    public void testSumInPlay()
    {
        underTest.setBuyIn(BUY_IN);
        CompetitorBO competitorBO_1 = underTest.register(player1);
        CompetitorBO competitorBO_2 = underTest.register(player2);
        underTest.buyin(competitorBO_1);
        underTest.buyin(competitorBO_2);
        assertEquals(Money.getInstance("10 EUR"), underTest.getSumInplay());
    }

    @Test
    public void testCompetitors()
    {
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
        List<TournamentRoundBO> rounds = underTest.getTournamentRounds();
        assertEquals("five rounds added", 5, rounds.size());
        assertNull("third round is pause", rounds.get(2).getBlindLevel());
        assertEquals("second round is level with small blind", Integer.valueOf(200), rounds.get(1).getBlindLevel().getSmallBlind());
        // remove pause
        underTest.remove(rounds.get(2));
        List<TournamentRoundBO> roundsAfterDelete = underTest.getTournamentRounds();
        assertEquals("four rounds left", 4, roundsAfterDelete.size());
        for(TournamentRoundBO roundBO : roundsAfterDelete)
        {
            assertNotNull("only levels left", roundBO.getBlindLevel());
        }
    }
}
