package de.hatoka.tournament.internal.dao;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.Date;
import java.util.Set;

import javax.inject.Inject;
import javax.persistence.EntityTransaction;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import com.google.inject.Guice;
import com.google.inject.Injector;

import de.hatoka.common.capi.dao.SequenceProvider;
import de.hatoka.common.capi.dao.TransactionProvider;
import de.hatoka.common.capi.modules.CommonDaoModule;
import de.hatoka.tournament.capi.dao.CompetitorDao;
import de.hatoka.tournament.capi.dao.PlayerDao;
import de.hatoka.tournament.capi.dao.TournamentDao;
import de.hatoka.tournament.capi.entities.CompetitorPO;
import de.hatoka.tournament.capi.entities.PlayerPO;
import de.hatoka.tournament.capi.entities.TournamentPO;
import de.hatoka.tournament.internal.modules.TournamentDaoJpaModule;

public class TournamentDaoJpaTest
{
    private static final String TEST_ACCOUNT_REF = "test_account_ref";

    @Rule
    public DerbyEntityManagerRule rule = new DerbyEntityManagerRule();
    @Inject
    private TournamentDao tournamentDao;
    @Inject
    private PlayerDao playerDao;
    @Inject
    private CompetitorDao competitorDao;

    @Inject
    private TransactionProvider transactionProvider;

    @Inject
    private SequenceProvider sequenceProvider;

    @Before
    public void createTestObject()
    {
        Injector injector = Guice.createInjector(new CommonDaoModule(), new TournamentDaoJpaModule(), rule.getModule());
        injector.injectMembers(this);
    }

    @Test
    public void testCRUD()
    {
        EntityTransaction transaction = transactionProvider.get();
        transaction.begin();
        TournamentPO tournamentPO = tournamentDao.createAndInsert(TEST_ACCOUNT_REF, createExternalRef(), "Tournament1", new Date(), false);
        PlayerPO playerPO = playerDao.createAndInsert(TEST_ACCOUNT_REF, createExternalRef(), "Player1");
        CompetitorPO competitorPO = competitorDao.createAndInsert(tournamentPO, playerPO);
        transaction.commit();
        Set<CompetitorPO> competitors = tournamentPO.getCompetitors();
        assertNotNull("competitor not assigned", competitors.contains(competitorPO));

        transaction.begin();
        playerDao.remove(playerPO);
        transaction.commit();

        assertTrue("delete doesn't work, player still exist", tournamentPO.getCompetitors().isEmpty());
    }

    private String createExternalRef()
    {
        return sequenceProvider.create(TEST_ACCOUNT_REF).generate();
    }
}
