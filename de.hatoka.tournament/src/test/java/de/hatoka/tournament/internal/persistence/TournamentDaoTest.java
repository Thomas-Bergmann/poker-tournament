package de.hatoka.tournament.internal.persistence;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import tests.de.hatoka.tournament.TournamentTestConfiguration;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = { TournamentTestConfiguration.class })
public class TournamentDaoTest
{
    private static final String TEST_USER_REF = "TournamentDaoTest-U";
    private static final String TEST_TOURNAMENT_NAME = "TournamentDaoTest-N";
    private static final String TEST_TOURNAMENT_REF = "TournamentDaoTest-T";
    private static final Date START_DATE = new Date();

    @Autowired
    private TournamentDao tournamentDao;

    @Test
    public void testCRUD()
    {
        TournamentPO tournamentPO = newTournamentPO(TEST_USER_REF, TEST_TOURNAMENT_REF, TEST_TOURNAMENT_NAME, START_DATE);
        tournamentDao.save(tournamentPO);
        Optional<TournamentPO> findTournamentPO= tournamentDao.findByOwnerRefAndLocalRef(TEST_USER_REF, TEST_TOURNAMENT_REF);
        assertEquals(tournamentPO, findTournamentPO.get());
        tournamentDao.delete(tournamentPO);
    }

    @Test
    public void listByUser()
    {
        TournamentPO tournament1PO = newTournamentPO(TEST_USER_REF, TEST_TOURNAMENT_REF + "1", TEST_TOURNAMENT_NAME + "1", START_DATE);
        tournamentDao.save(tournament1PO);
        TournamentPO tournament2PO = newTournamentPO(TEST_USER_REF, TEST_TOURNAMENT_REF+ "2", TEST_TOURNAMENT_NAME + "2", START_DATE);
        tournamentDao.save(tournament2PO);
        List<TournamentPO> findTournaments = tournamentDao.getByOwnerRef(TEST_USER_REF);
        assertEquals(2, findTournaments.size());
        findTournaments.forEach(tournamentDao::delete);
    }

    private TournamentPO newTournamentPO(String testUserRef, String localRef, String name, Date startDate)
    {
        TournamentPO result = new TournamentPO();
        result.setOwnerRef(testUserRef);
        result.setLocalRef(localRef);
        result.setName(name);
        result.setStartDate(startDate);
        return result;
    }
}
