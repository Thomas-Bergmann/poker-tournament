package de.hatoka.tournament.capi.business;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.Date;

import javax.inject.Inject;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import de.hatoka.tournament.internal.dao.DerbyEntityManagerRule;

public class TournamentBOTest
{
    private static final String ACCOUNT_REF = TournamentBOTest.class.getSimpleName();

    @Rule
    public DerbyEntityManagerRule rule = new DerbyEntityManagerRule();

    @Inject
    private TournamentBusinessFactory factory;

    private TournamentBO UNDER_TEST;

    @Before
    public void createTestObject()
    {
        TestBusinessInjectorProvider.get(rule.getModule()).injectMembers(this);
        TournamentBORepository tournamentBORepository = factory.getTournamentBORepository(ACCOUNT_REF);
        UNDER_TEST = tournamentBORepository.create("test", new Date());
    }

    @Test
    public void testAssignUnassign()
    {
        CompetitorBO competitorBO = UNDER_TEST.assign(factory.getPlayerBORepository(ACCOUNT_REF).create("testPlayer1"));
        assertTrue(UNDER_TEST.getCompetitors().contains(competitorBO));
        UNDER_TEST.unassign(competitorBO);
        assertFalse(UNDER_TEST.getCompetitors().contains(competitorBO));
    }
}
