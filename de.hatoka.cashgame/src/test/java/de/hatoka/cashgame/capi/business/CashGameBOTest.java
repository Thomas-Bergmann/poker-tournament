package de.hatoka.cashgame.capi.business;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import de.hatoka.common.capi.math.Money;
import de.hatoka.player.capi.business.PlayerBO;
import de.hatoka.player.capi.business.PlayerBORepository;
import de.hatoka.player.capi.business.PlayerRef;
import de.hatoka.player.capi.types.HistoryEntryType;
import de.hatoka.user.capi.business.UserRef;
import tests.de.hatoka.cashgame.CashGameTestConfiguration;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = { CashGameTestConfiguration.class })
public class CashGameBOTest
{
    private static final Money USD_4  = Money.valueOf("4 USD");
    private static final Money USD_5  = Money.valueOf("5 USD");
    private static final Money USD_6  = Money.valueOf("6 USD");
    private static final Money USD_7  = Money.valueOf("7 USD");
    private static final Money USD_7_50  = Money.valueOf("7.5 USD");
    private static final Money USD_10 = Money.valueOf("10 USD");
    private static final Money USD_11 = Money.valueOf("11 USD");
    private static final UserRef USER_REF = UserRef.valueOfLocal(CashGameBOTest.class.getSimpleName());
    private static final PlayerRef PLAYER_REF_ONE = PlayerRef.valueOf(USER_REF, "player-one");
    private static final PlayerRef PLAYER_REF_TWO = PlayerRef.valueOf(USER_REF, "player-two");
    private static final PlayerRef PLAYER_REF_THREE = PlayerRef.valueOf(USER_REF, "player-three");

    private static final Date CURRENT_DATE = new Date();

    @Autowired
    private CashGameBORepository cashGameRepo;
    @Autowired
    private PlayerBORepository playerRepo;

    private CashGameBO cashGameBO;
    private List<PlayerBO> createdPlayer = new ArrayList<>();

    @BeforeEach
    public void createTestObject()
    {
        cashGameBO = cashGameRepo.createCashGame(USER_REF, "TEST", CURRENT_DATE);
        cashGameBO.setBuyIn(USD_5);
    }

    @AfterEach
    public void removeTestObject()
    {
        createdPlayer.forEach(PlayerBO::remove);
        cashGameBO.remove();
    }

    /**
     *                inPlay
     *                    result
     * buyin        5 5    -5
     * rebuy        5 10   -10
     * seat open    4 6   -6
     * buyin        5 11  -6
     * seat open    4 7   -7
     */
    @Test
    public void testBuyInSeatOpenProcess()
    {
        PlayerBO playerBO = playerRepo.createPlayer(PLAYER_REF_ONE, "player1");
        createdPlayer.add(playerBO);
        CompetitorBO competitorBO = cashGameBO.sitDown(playerBO, USD_5);
        assertTrue(competitorBO.isActive(), "is active");
        assertEquals(USD_5, competitorBO.getInPlay(), "invested 5 cashout 0");

        competitorBO.rebuy(USD_5);
        assertEquals(USD_10, competitorBO.getInPlay(), "invested 10 cashout 0");
        assertTrue(competitorBO.isActive(), "is active");

        competitorBO.payout(USD_4);
        assertEquals(USD_6, competitorBO.getInPlay(), "invested 10 cashout 4");
        assertFalse(competitorBO.isActive(), "is not active");

        competitorBO.buyin(USD_5);
        assertEquals(USD_11, competitorBO.getInPlay(), "invested 15 cashout 4");
        assertTrue(competitorBO.isActive(), "is active");

        competitorBO.payout(USD_4);
        assertEquals(USD_7, competitorBO.getInPlay(), "invested 15 cashout 8");
        assertFalse(competitorBO.isActive(), "is not active");

        assertEquals(5, cashGameBO.getHistoryEntries().size(), "count history entries");
        assertEquals(2, cashGameBO.getHistoryEntries().stream().filter(entry -> entry.getType().equals(HistoryEntryType.BuyIn)).count(), "buy-ins");
    }

    @Test
    public void testGetAverageStack()
    {
        PlayerBO player1BO = playerRepo.createPlayer(PLAYER_REF_ONE, "player1");
        createdPlayer.add(player1BO);
        cashGameBO.sitDown(player1BO, USD_5);
        PlayerBO player2BO = playerRepo.createPlayer(PLAYER_REF_TWO , "player2");
        createdPlayer.add(player2BO);
        cashGameBO.sitDown(player2BO, USD_5);
        PlayerBO player3BO = playerRepo.createPlayer(PLAYER_REF_THREE, "player3");
        createdPlayer.add(player3BO);
        CompetitorBO competitor3BO = cashGameBO.sitDown(player3BO, USD_5);
        assertEquals(USD_5, cashGameBO.getAverageInplay());
        competitor3BO.payout(Money.NOTHING);
        assertEquals(USD_7_50, cashGameBO.getAverageInplay());
    }

}
