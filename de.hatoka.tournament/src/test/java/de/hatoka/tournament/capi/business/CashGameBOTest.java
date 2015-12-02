package de.hatoka.tournament.capi.business;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.Date;

import javax.inject.Inject;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import com.google.inject.Guice;
import com.google.inject.Injector;

import de.hatoka.common.capi.business.Money;
import de.hatoka.common.capi.modules.CommonDaoModule;
import de.hatoka.test.DerbyEntityManagerRule;
import de.hatoka.tournament.capi.types.HistoryEntryType;
import de.hatoka.tournament.internal.modules.TournamentBusinessModule;
import de.hatoka.tournament.internal.modules.TournamentDaoJpaModule;

public class CashGameBOTest
{
    private static final Money USD_4  = Money.valueOf("4 USD");
    private static final Money USD_5  = Money.valueOf("5 USD");
    private static final Money USD_6  = Money.valueOf("6 USD");
    private static final Money USD_7  = Money.valueOf("7 USD");
    private static final Money USD_10 = Money.valueOf("10 USD");
    private static final Money USD_11 = Money.valueOf("11 USD");
    private static final Money USD_6_MINUS  = Money.valueOf("-6 USD");
    private static final Money USD_7_MINUS  = Money.valueOf("-7 USD");
    private static final String ACCOUNT_REF = "TEST_ACCOUNT_REF";
    private static final Date CURRENT_DATE = new Date();

    private CashGameBO cashGameBO;

    @Rule
    public DerbyEntityManagerRule rule = new DerbyEntityManagerRule();

    @Inject
    private TournamentBusinessFactory factory;

    @Before
    public void createTestObject()
    {
        Injector injector = Guice.createInjector(new CommonDaoModule(), new TournamentDaoJpaModule(), new TournamentBusinessModule(), rule.getModule());
        injector.injectMembers(this);
        cashGameBO = factory.getTournamentBORepository(ACCOUNT_REF).createCashGame(CURRENT_DATE);
        cashGameBO.setBuyIn(Money.valueOf("5 EUR"));
    }

    /**
     *                inplay
     *                    result
     * buyin        5 5    0
     * rebuy        5 10   0
     * seat open    4 6   -6
     * buyin        5 11  -6
     * seat open    4 7   -7
     */
    @Test
    public void testBuyInSeatOpenProcess()
    {
        PlayerBO playerBO = factory.getPlayerBORepository(ACCOUNT_REF).create("player1");
        CashGameCompetitorBO competitorBO = cashGameBO.sitDown(playerBO, USD_5);
        assertTrue("is active", competitorBO.isActive());
        assertEquals("buy in booked", USD_5, competitorBO.getInPlay());
        assertEquals("result empty", Money.NOTHING, competitorBO.getResult());

        competitorBO.rebuy(USD_5);
        assertEquals("buy in booked", USD_10, competitorBO.getInPlay());
        assertEquals("result empty", Money.NOTHING, competitorBO.getResult());
        assertTrue("is active", competitorBO.isActive());

        cashGameBO.seatOpen(competitorBO, USD_4);
        assertEquals("buy in booked", USD_6, competitorBO.getInPlay());
        assertEquals("result empty", USD_6_MINUS, competitorBO.getResult());
        assertFalse("is active", competitorBO.isActive());

        competitorBO.buyin(USD_5);
        assertEquals("buy in booked", USD_11, competitorBO.getInPlay());
        assertEquals("result empty", USD_6_MINUS, competitorBO.getResult());
        assertTrue("is active", competitorBO.isActive());

        cashGameBO.seatOpen(competitorBO, USD_4);
        assertEquals("buy in booked", USD_7, competitorBO.getInPlay());
        assertEquals("result empty", USD_7_MINUS, competitorBO.getResult());
        assertFalse("is active", competitorBO.isActive());

        assertEquals("count history entries", 5, cashGameBO.getHistoryEntries().size());
        assertEquals("buy-ins", 2, cashGameBO.getHistoryEntries().stream().filter(entry -> entry.getType().equals(HistoryEntryType.BuyIn)).count());
    }
}
