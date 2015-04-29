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
import de.hatoka.tournament.capi.dao.CompetitorDao;
import de.hatoka.tournament.capi.dao.PlayerDao;
import de.hatoka.tournament.capi.dao.TournamentDao;
import de.hatoka.tournament.capi.entities.CompetitorPO;
import de.hatoka.tournament.capi.entities.HistoryEntryType;
import de.hatoka.tournament.capi.entities.PlayerPO;
import de.hatoka.tournament.capi.entities.TournamentPO;
import de.hatoka.tournament.internal.dao.DerbyEntityManagerRule;
import de.hatoka.tournament.internal.modules.TournamentBusinessModule;
import de.hatoka.tournament.internal.modules.TournamentDaoJpaModule;

public class CompetitorBOTest
{
    private static final Money USD_4  = Money.getInstance("4 USD");
    private static final Money USD_5  = Money.getInstance("5 USD");
    private static final Money USD_6  = Money.getInstance("6 USD");
    private static final Money USD_7  = Money.getInstance("7 USD");
    private static final Money USD_10 = Money.getInstance("10 USD");
    private static final Money USD_11 = Money.getInstance("11 USD");
    private static final Money USD_6_MINUS  = Money.getInstance("-6 USD");
    private static final Money USD_7_MINUS  = Money.getInstance("-7 USD");
    private static final String ACCOUNT_REF = "TEST_ACCOUNT_REF";
    private static final Date CURRENT_DATE = new Date();

    private CompetitorBO UNDER_TEST;
    private CashGameBO cashGameBO;

    @Rule
    public DerbyEntityManagerRule rule = new DerbyEntityManagerRule();

    @Inject
    private TournamentBusinessFactory factory;
    @Inject
    private PlayerDao playerDao;
    @Inject
    private TournamentDao tournamentDao;
    @Inject
    private CompetitorDao competitorDao;


    @Before
    public void createTestObject()
    {
        Injector injector = Guice.createInjector(new CommonDaoModule(), new TournamentDaoJpaModule(), new TournamentBusinessModule(), rule.getModule());
        injector.injectMembers(this);
        PlayerPO playerPO = playerDao.createAndInsert(ACCOUNT_REF, "player1");
        TournamentPO tournamentPO = tournamentDao.createAndInsert(ACCOUNT_REF, "tournament", CURRENT_DATE, true);
        tournamentPO.setBuyIn(Money.getInstance("5 EUR").toMoneyPO());
        CompetitorPO competitorPO = competitorDao.createAndInsert(tournamentPO, playerPO);
        cashGameBO = factory.getCashGameBO(competitorPO.getTournamentPO());
        UNDER_TEST = factory.getCompetitorBO(competitorPO, cashGameBO);
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
        assertFalse("is active", UNDER_TEST.isActive());

        UNDER_TEST.buyin(USD_5);
        assertEquals("buy in booked", USD_5, UNDER_TEST.getInPlay());
        assertEquals("result empty", Money.NOTHING, UNDER_TEST.getResult());
        assertTrue("is active", UNDER_TEST.isActive());

        UNDER_TEST.rebuy(USD_5);
        assertEquals("buy in booked", USD_10, UNDER_TEST.getInPlay());
        assertEquals("result empty", Money.NOTHING, UNDER_TEST.getResult());
        assertTrue("is active", UNDER_TEST.isActive());

        cashGameBO.seatOpen(UNDER_TEST, USD_4);
        assertEquals("buy in booked", USD_6, UNDER_TEST.getInPlay());
        assertEquals("result empty", USD_6_MINUS, UNDER_TEST.getResult());
        assertFalse("is active", UNDER_TEST.isActive());

        UNDER_TEST.buyin(USD_5);
        assertEquals("buy in booked", USD_11, UNDER_TEST.getInPlay());
        assertEquals("result empty", USD_6_MINUS, UNDER_TEST.getResult());
        assertTrue("is active", UNDER_TEST.isActive());

        cashGameBO.seatOpen(UNDER_TEST, USD_4);
        assertEquals("buy in booked", USD_7, UNDER_TEST.getInPlay());
        assertEquals("result empty", USD_7_MINUS, UNDER_TEST.getResult());
        assertFalse("is active", UNDER_TEST.isActive());

        assertEquals("count history entries", 5, cashGameBO.getHistoryEntries().size());
        assertEquals("buy-ins", 2, cashGameBO.getHistoryEntries().stream().filter(entry -> entry.getType().equals(HistoryEntryType.BuyIn)).count());
    }
}
