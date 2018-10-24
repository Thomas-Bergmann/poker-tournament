package de.hatoka.cashgame.internal.persistence;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import tests.de.hatoka.cashgame.CashGameTestConfiguration;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = { CashGameTestConfiguration.class })
public class CashGameDaoTest
{
    private static final String TEST_USER_REF = "TournamentDaoTest-U";
    private static final String TEST_CASHGAME_REF = "TournamentDaoTest-N";
    private static final Date START_DATE = new Date();

    @Autowired
    private CashGameDao tournamentDao;

    @Test
    public void testCRUD()
    {
        CashGamePO tournamentPO = newCashGame(TEST_USER_REF, TEST_CASHGAME_REF, START_DATE);
        tournamentDao.save(tournamentPO);
        Optional<CashGamePO> findTournamentPO= tournamentDao.findByOwnerRefAndLocalRef(TEST_USER_REF, TEST_CASHGAME_REF);
        assertEquals(tournamentPO, findTournamentPO.get());
        tournamentDao.delete(tournamentPO);
    }

    @Test
    public void listByUser()
    {
        CashGamePO tournament1PO = newCashGame(TEST_USER_REF, TEST_CASHGAME_REF + "1", START_DATE);
        tournamentDao.save(tournament1PO);
        CashGamePO tournament2PO = newCashGame(TEST_USER_REF, TEST_CASHGAME_REF + "2", START_DATE);
        tournamentDao.save(tournament2PO);
        List<CashGamePO> findTournaments = tournamentDao.getByOwnerRef(TEST_USER_REF);
        assertEquals(2, findTournaments.size());
        findTournaments.forEach(tournamentDao::delete);
    }

    private CashGamePO newCashGame(String testUserRef, String localRef, Date startDate)
    {
        CashGamePO result = new CashGamePO();
        result.setOwnerRef(testUserRef);
        result.setLocalRef(localRef);
        result.setStartDate(startDate);
        return result;
    }
}
