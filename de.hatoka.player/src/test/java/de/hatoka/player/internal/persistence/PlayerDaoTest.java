package de.hatoka.player.internal.persistence;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import tests.de.hatoka.player.PlayerTestConfiguration;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = { PlayerTestConfiguration.class })
public class PlayerDaoTest
{
    private static final String TEST_USER_REF = "PlayerDaoTest-U";
    private static final String TEST_PLAYER_NAME = "PlayerDaoTest-N";
    private static final String TEST_PLAYER_REF = "PlayerDaoTest-T";
    private static final Date START_DATE = new Date();

    @Autowired
    private PlayerDao playerDao;

    @Test
    public void testCRUD()
    {
        PlayerPO PlayerPO = newPlayerPO(TEST_USER_REF, TEST_PLAYER_REF, false, TEST_PLAYER_NAME, START_DATE);
        playerDao.save(PlayerPO);
        Optional<PlayerPO> findPlayerPO= playerDao.findByContextRefAndPlayerRef(TEST_USER_REF, TEST_PLAYER_REF);
        assertEquals(PlayerPO, findPlayerPO.get());
        playerDao.delete(PlayerPO);
    }

    @Test
    public void listByUser()
    {
        PlayerPO Player1PO = newPlayerPO(TEST_USER_REF, TEST_PLAYER_REF + "1", false, TEST_PLAYER_NAME + "1", START_DATE);
        playerDao.save(Player1PO);
        PlayerPO Player2PO = newPlayerPO(TEST_USER_REF, TEST_PLAYER_REF+ "2", false, TEST_PLAYER_NAME + "2", START_DATE);
        playerDao.save(Player2PO);
        List<PlayerPO> findPlayers = playerDao.getByContextRef(TEST_USER_REF);
        assertEquals(2, findPlayers.size());
        findPlayers.forEach(playerDao::delete);
    }

    private PlayerPO newPlayerPO(String testUserRef, String localRef, boolean isCashGame, String name, Date startDate)
    {
        PlayerPO result = new PlayerPO();
        result.setContextRef(testUserRef);
        result.setPlayerRef(localRef);
        result.setName(name);
        result.setFirstDate(startDate);
        return result;
    }
}
