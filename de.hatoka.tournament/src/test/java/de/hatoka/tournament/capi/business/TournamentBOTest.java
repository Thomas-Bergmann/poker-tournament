package de.hatoka.tournament.capi.business;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.Date;

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

    private TournamentBO underTest;
    private PlayerBO player1;
    private PlayerBO player2;

    @Before
    public void createTestObject()
    {
        TestBusinessInjectorProvider.get(rule.getModule()).injectMembers(this);
        TournamentBORepository tournamentBORepository = factory.getTournamentBORepository(ACCOUNT_REF);
        underTest = tournamentBORepository.create("test", NOW);
        player1 = factory.getPlayerBORepository(ACCOUNT_REF).create("testPlayer1");
        player2 = factory.getPlayerBORepository(ACCOUNT_REF).create("testPlayer2");
    }

    @Test
    public void testAssignUnassign()
    {
        CompetitorBO competitorBO = underTest.assign(player1);
        assertTrue(underTest.getCompetitors().contains(competitorBO));
        underTest.unassign(competitorBO);
        assertFalse(underTest.getCompetitors().contains(competitorBO));
    }

    @Test
    public void testSumInPlay()
    {
        underTest.setBuyIn(BUY_IN);
        CompetitorBO competitorBO_1 = underTest.assign(player1);
        CompetitorBO competitorBO_2 = underTest.assign(player2);
        competitorBO_1.buyin(underTest.getBuyIn());
        competitorBO_2.buyin(Money.getInstance("15 EUR"));
        assertEquals(Money.getInstance("20 EUR"), underTest.getSumInplay());
    }

    @Test
    public void testCompetitors()
    {
        CompetitorBO competitorBO_1 = underTest.assign(player1);
        assertTrue("player one is competitor", underTest.isCompetitor(player1));
        assertFalse("player two is not competitor", underTest.isCompetitor(player2));
        CompetitorBO competitorBO_2 = underTest.assign(player2);
        competitorBO_2.buyin(BUY_IN);
        assertFalse("player one is not active", underTest.getActiveCompetitors().contains(competitorBO_1));
        assertTrue("player two is active", underTest.getActiveCompetitors().contains(competitorBO_2));
        assertEquals("correct player assigned", player1, competitorBO_1.getPlayerBO());
    }

    @Test
    public void testSimpleAttributes()
    {
        assertEquals(NOW, underTest.getDate());
    }
}
