package de.hatoka.tournament.capi.business;

import java.io.File;
import java.io.FileReader;
import java.util.Date;
import java.util.List;

import javax.inject.Inject;
import javax.persistence.EntityTransaction;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.Unmarshaller;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Rule;
import org.junit.Test;

import com.google.inject.Guice;
import com.google.inject.Injector;

import de.hatoka.common.capi.business.Money;
import de.hatoka.common.capi.dao.TransactionProvider;
import de.hatoka.common.capi.modules.CommonDaoModule;
import de.hatoka.tournament.capi.dao.CompetitorDao;
import de.hatoka.tournament.capi.dao.PlayerDao;
import de.hatoka.tournament.capi.dao.TournamentDao;
import de.hatoka.tournament.capi.entities.CompetitorPO;
import de.hatoka.tournament.capi.entities.PlayerPO;
import de.hatoka.tournament.capi.entities.TournamentModel;
import de.hatoka.tournament.capi.entities.TournamentPO;
import de.hatoka.tournament.internal.business.TournamentBORepositoryImpl;
import de.hatoka.tournament.internal.dao.DerbyEntityManagerRule;
import de.hatoka.tournament.internal.modules.TournamentDaoJpaModule;

public class TournamentRepositoryBOTest
{
    private static final String ACCOUNT_REF = "accountRef_OK";
    private static final String ACCOUNT_REF_OTHER = "accountRef_WRONG";
    private static final Date CURRENT_DATE = new Date();

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

    @Before
    public void createTestObject()
    {
        Injector injector = Guice.createInjector(new CommonDaoModule(), new TournamentDaoJpaModule(), rule.getModule());
        injector.injectMembers(this);
    }

    @Test
    public void testExport() throws Exception
    {
        createData(ACCOUNT_REF);
        File exportFile = new TournamentBORepositoryImpl(ACCOUNT_REF, tournamentDao, playerDao, competitorDao, null).exportFile();
        validateData(ACCOUNT_REF, exportFile, true);
    }

    @Test
    @Ignore
    public void testImport() throws Exception
    {
        createData(ACCOUNT_REF_OTHER);
        File exportFile = new TournamentBORepositoryImpl(ACCOUNT_REF_OTHER, tournamentDao, playerDao, competitorDao, null).exportFile();
        List<String> warnings = new TournamentBORepositoryImpl(ACCOUNT_REF, tournamentDao, playerDao, competitorDao, null).importFile(exportFile);
        for(String warning : warnings)
        {
            System.out.println(warning);
        }
        validateData(ACCOUNT_REF, exportFile, false);
    }
    private void validateData(String accountRef, File exportFile, boolean checkAccountRef) throws Exception
    {
        JAXBContext context = JAXBContext.newInstance(TournamentModel.class);
        Unmarshaller um = context.createUnmarshaller();
        TournamentModel tournamentModel = (TournamentModel)um.unmarshal(new FileReader(exportFile));
        if (checkAccountRef)
        {
            Assert.assertEquals(accountRef, tournamentModel.getAccountRef());
        }
        for (PlayerPO playerXML : tournamentModel.getPlayerPOs())
        {
            playerXML.setAccountRef(accountRef); // accountRef not stored at each element
            PlayerPO playerPO = playerDao.getById(playerXML.getId());
            Assert.assertEquals(playerXML.getName(), playerPO, playerXML);
        }
        for (TournamentPO tournamentXML : tournamentModel.getTournamentPOs())
        {
            tournamentXML.setAccountRef(accountRef); // accountRef not stored at each element
            TournamentPO tournamentPO = tournamentDao.getById(tournamentXML.getId());
            Assert.assertEquals(tournamentXML.getId(), tournamentPO, tournamentXML);
            for(CompetitorPO competitorXML : tournamentXML.getCompetitors())
            {
                Assert.assertTrue(competitorXML.getId(), tournamentPO.getCompetitors().contains(competitorXML));
            }
        }
    }

    private void createData(String accountRef)
    {
        EntityTransaction entityTransaction = transactionProvider.get();
        entityTransaction.begin();
        PlayerPO player1 = playerDao.createAndInsert(accountRef, "player1");
        TournamentPO tournamentPO = tournamentDao.createAndInsert(accountRef, "tournament", CURRENT_DATE, false);
        tournamentPO.setBuyIn(Money.getInstance("5 EUR").toMoneyPO());
        CompetitorPO competitor1 = competitorDao.createAndInsert(tournamentPO, player1);
        competitor1.setMoneyInPlay(Money.getInstance("5 EUR").toMoneyPO());
        competitor1.setMoneyResult(Money.getInstance("-5 EUR").toMoneyPO());
        entityTransaction.commit();
    }

}
