package de.hatoka.tournament.capi.business;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import de.hatoka.common.capi.business.Money;
import de.hatoka.tournament.capi.dao.CompetitorDao;
import de.hatoka.tournament.capi.entities.CompetitorPO;
import de.hatoka.tournament.internal.business.CompetitorBOImpl;

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

    private CompetitorBO UNDER_TEST;

    @Mock
    private TournamentBusinessFactory factory;
    @Mock
    private CompetitorDao competitorDao;
    @Mock
    private TournamentBO tournamentBO;
    @Before
    public void createTestObject()
    {
        MockitoAnnotations.initMocks(this);
        CompetitorPO competitorPO = new CompetitorPO();
        UNDER_TEST = new CompetitorBOImpl(competitorPO, tournamentBO, factory);
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

        UNDER_TEST.seatOpen(USD_4);
        assertEquals("buy in booked", USD_6, UNDER_TEST.getInPlay());
        assertEquals("result empty", USD_6_MINUS, UNDER_TEST.getResult());
        assertFalse("is active", UNDER_TEST.isActive());

        UNDER_TEST.buyin(USD_5);
        assertEquals("buy in booked", USD_11, UNDER_TEST.getInPlay());
        assertEquals("result empty", USD_6_MINUS, UNDER_TEST.getResult());
        assertTrue("is active", UNDER_TEST.isActive());

        UNDER_TEST.seatOpen(USD_4);
        assertEquals("buy in booked", USD_7, UNDER_TEST.getInPlay());
        assertEquals("result empty", USD_7_MINUS, UNDER_TEST.getResult());
        assertFalse("is active", UNDER_TEST.isActive());
    }
}
