package de.hatoka.player.capi.business;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import de.hatoka.user.capi.business.UserRef;
import tests.de.hatoka.player.PlayerTestConfiguration;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = { PlayerTestConfiguration.class })
public class PlayerBORepositoryTest
{
    private static final UserRef USER_REF = UserRef.valueOfLocal(PlayerBORepositoryTest.class.getSimpleName());
    private static final PlayerRef PLAYER_REF_ONE = PlayerRef.valueOf(USER_REF, "player-one");
    private static final PlayerRef PLAYER_REF_TWO = PlayerRef.valueOf(USER_REF, "player-two");

    @Autowired
    private PlayerBORepository repository;

    @Test
    public void testCrud()
    {
        PlayerBO player1 = repository.createPlayer(PLAYER_REF_ONE, "ready-player-one");
        PlayerBO player2 = repository.createPlayer(PLAYER_REF_TWO, "ready-player-two");
        List<PlayerBO> players = repository.getPlayers(USER_REF);
        assertEquals(2, players.size());
        assertTrue(players.contains(player1));
        assertTrue(players.contains(player2));
        player1.remove();
        players = repository.getPlayers(USER_REF);
        assertEquals(1, players.size());
        player2.remove();
        players = repository.getPlayers(USER_REF);
        assertTrue(players.isEmpty());
    }

}
