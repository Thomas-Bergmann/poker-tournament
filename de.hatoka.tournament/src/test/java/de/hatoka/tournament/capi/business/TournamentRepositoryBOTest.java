package de.hatoka.tournament.capi.business;

import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.inject.Inject;
import javax.persistence.EntityTransaction;

import org.custommonkey.xmlunit.XMLAssert;
import org.custommonkey.xmlunit.XMLUnit;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;

import com.google.inject.Guice;
import com.google.inject.Injector;

import de.hatoka.common.capi.business.CountryHelper;
import de.hatoka.common.capi.business.Money;
import de.hatoka.common.capi.dao.SequenceProvider;
import de.hatoka.common.capi.dao.TransactionProvider;
import de.hatoka.common.capi.dao.UUIDGenerator;
import de.hatoka.common.capi.modules.CommonDaoModule;
import de.hatoka.common.capi.resource.LocalizationConstants;
import de.hatoka.common.capi.resource.ResourceLoader;
import de.hatoka.tournament.capi.dao.BlindLevelDao;
import de.hatoka.tournament.capi.dao.CompetitorDao;
import de.hatoka.tournament.capi.dao.HistoryDao;
import de.hatoka.tournament.capi.dao.PlayerDao;
import de.hatoka.tournament.capi.dao.TournamentDao;
import de.hatoka.tournament.capi.entities.CompetitorPO;
import de.hatoka.tournament.capi.entities.HistoryPO;
import de.hatoka.tournament.capi.entities.PlayerPO;
import de.hatoka.tournament.capi.entities.TournamentPO;
import de.hatoka.tournament.capi.types.HistoryEntryType;
import de.hatoka.tournament.internal.business.TournamentBORepositoryImpl;
import de.hatoka.tournament.internal.dao.DerbyEntityManagerRule;
import de.hatoka.tournament.internal.modules.TournamentBusinessModule;
import de.hatoka.tournament.internal.modules.TournamentDaoJpaModule;

public class TournamentRepositoryBOTest
{
    private static final String RESOURCE_PREFIX = "de/hatoka/tournament/internal/business/";
    private static final String ACCOUNT_REF = "accountRef_OK";
    private static final Date CURRENT_DATE = parseDate("2011-11-25T08:45");
    private static final ResourceLoader RESOURCE_LOADER = new ResourceLoader();

    @Rule
    public DerbyEntityManagerRule rule = new DerbyEntityManagerRule();
    @Inject
    private TournamentDao tournamentDao;
    @Inject
    private PlayerDao playerDao;
    @Inject
    private CompetitorDao competitorDao;
    @Inject
    private HistoryDao historyDao;
    @Inject
    private BlindLevelDao blindLevelDao;

    @Inject
    private TransactionProvider transactionProvider;

    @Inject
    private TournamentBusinessFactory factory;
    @Inject
    private UUIDGenerator uuidGenerator;
    @Inject
    private SequenceProvider sequenceProvider;

    @Before
    public void createTestObject()
    {
        Injector injector = Guice.createInjector(new CommonDaoModule(100), new TournamentDaoJpaModule(), new TournamentBusinessModule(), rule.getModule());
        injector.injectMembers(this);
        getRepository().getTournaments().iterator().forEachRemaining(bo -> bo.remove());
    }

    @BeforeClass
    public static void initClass()
    {
        XMLUnit.setIgnoreWhitespace(true);
    }

    private String getResource(String resource) throws IOException
    {
        return RESOURCE_LOADER.getResourceAsString(RESOURCE_PREFIX + resource);
    }

    private InputStream getResourceStream(String resource) throws IOException
    {
        return RESOURCE_LOADER.getResourceAsStream(RESOURCE_PREFIX + resource);
    }

    @Test
    public void testExport() throws Exception
    {
        createData(ACCOUNT_REF);
        StringWriter writer = new StringWriter();
        getRepository().exportXML(writer);
        // Assert.assertEquals("export correct", getResource("tournament_export.result.xml"), writer.toString());
        XMLAssert.assertXMLEqual("export correct", getResource("tournament_export.result.xml"), writer.toString());
    }

    private TournamentBORepositoryImpl getRepository()
    {
        return new TournamentBORepositoryImpl(ACCOUNT_REF, tournamentDao, playerDao, competitorDao, uuidGenerator, sequenceProvider.create(ACCOUNT_REF), factory);
    }

    @Test
    public void testImport() throws Exception
    {
        TournamentBORepositoryImpl repository = getRepository();
        List<String> warnings = repository.importXML(getResourceStream("tournament_export.result.xml"));
        StringWriter writer = new StringWriter();
        repository.exportXML(writer);
        Assert.assertEquals("export correct", getResource("tournament_export.result.xml"), writer.toString());
        for(String warning : warnings)
        {
            Assert.assertNull(warning);
        }
    }

    private void createData(String accountRef)
    {
        EntityTransaction entityTransaction = transactionProvider.get();
        entityTransaction.begin();
        PlayerPO player1 = playerDao.createAndInsert(accountRef, sequenceProvider.create(ACCOUNT_REF).generate(), "player1");
        TournamentPO tournamentPO = tournamentDao.createAndInsert(accountRef, sequenceProvider.create(ACCOUNT_REF).generate(), "tournament", CURRENT_DATE, false);
        tournamentPO.setBuyIn(Money.getInstance("5 EUR").toMoneyPO());
        CompetitorPO competitor1 = competitorDao.createAndInsert(tournamentPO, player1);
        competitor1.setMoneyInPlay(Money.getInstance("5 EUR").toMoneyPO());
        competitor1.setMoneyResult(Money.getInstance("-5 EUR").toMoneyPO());

        HistoryPO historyEntry = historyDao.createAndInsert(tournamentPO, player1, CURRENT_DATE);
        historyEntry.setActionKey(HistoryEntryType.BuyIn.name());

        blindLevelDao.createAndInsert(tournamentPO, 30);
        entityTransaction.commit();
    }

    private static Date parseDate(String dateString)
    {
        SimpleDateFormat result = new SimpleDateFormat(LocalizationConstants.XML_DATEFORMAT_MINUTES);
        result.setTimeZone(CountryHelper.TZ_BERLIN);
        try
        {
            return result.parse(dateString);
        }
        catch(ParseException e)
        {
            throw new RuntimeException(e);
        }
    }
}
