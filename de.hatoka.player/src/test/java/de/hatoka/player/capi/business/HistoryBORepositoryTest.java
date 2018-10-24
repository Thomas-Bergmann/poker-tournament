package de.hatoka.player.capi.business;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Date;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import de.hatoka.common.capi.math.Money;
import de.hatoka.player.capi.types.HistoryEntryType;
import de.hatoka.user.capi.business.UserRef;
import tests.de.hatoka.player.PlayerTestConfiguration;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = { PlayerTestConfiguration.class })
public class HistoryBORepositoryTest
{
    private static final String CASHGAME_TEST = "cashgame:test";
    private static final UserRef USER_REF = UserRef.valueOfLocal(HistoryBORepository.class.getSimpleName());
    private static final PlayerRef PLAYER_REF_ONE = PlayerRef.valueOf(USER_REF, "player-one");
    private static final PlayerRef PLAYER_REF_TWO = PlayerRef.valueOf(USER_REF, "player-two");
    private static final Money EUR_5 = Money.valueOf("5 EUR");
    private static final Money EUR_10 = Money.valueOf("10 EUR");

    @Autowired
    private PlayerBORepository playerRepository;

    @Autowired
    private HistoryBORepository repository;

    @Test
    public void testCrud()
    {
        PlayerBO player1 = playerRepository.createPlayer(PLAYER_REF_ONE, "ready-player-one");
        PlayerBO player2 = playerRepository.createPlayer(PLAYER_REF_TWO, "ready-player-two");
        repository.createEntry(new Date(), player1.getRef(), CASHGAME_TEST, HistoryEntryType.BuyIn, EUR_5);
        repository.createEntry(new Date(), player2.getRef(), CASHGAME_TEST, HistoryEntryType.BuyIn, EUR_5);
        repository.createEntry(new Date(), player1.getRef(), CASHGAME_TEST, HistoryEntryType.ReBuy, EUR_5);
        repository.createEntry(new Date(), player2.getRef(), CASHGAME_TEST, HistoryEntryType.CashOut, EUR_10);
        repository.createEntry(new Date(), player1.getRef(), CASHGAME_TEST, HistoryEntryType.CashOut, EUR_5);

        List<HistoryEntryBO> entriesPlayer1 = repository.getEntries(player1.getRef());
        assertEquals(3, entriesPlayer1.size());
        List<HistoryEntryBO> entriesPlayer2 = repository.getEntries(player2.getRef());
        assertEquals(2, entriesPlayer2.size());
        List<HistoryEntryBO> entriesPlayerGame = repository.getEntries(CASHGAME_TEST);
        assertEquals(5, entriesPlayerGame.size());
    }

}
