package de.hatoka.tournament.internal.business;

import java.io.IOException;
import java.io.InputStream;
import java.io.Writer;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

import de.hatoka.common.capi.business.Warning;
import de.hatoka.common.capi.dao.TransactionProvider;
import de.hatoka.common.capi.dao.UUIDGenerator;
import de.hatoka.tournament.capi.business.CashGameBO;
import de.hatoka.tournament.capi.business.TournamentBO;
import de.hatoka.tournament.capi.business.TournamentBORepository;
import de.hatoka.tournament.capi.business.TournamentBusinessFactory;
import de.hatoka.tournament.capi.dao.BlindLevelDao;
import de.hatoka.tournament.capi.dao.CompetitorDao;
import de.hatoka.tournament.capi.dao.HistoryDao;
import de.hatoka.tournament.capi.dao.PlayerDao;
import de.hatoka.tournament.capi.dao.RankDao;
import de.hatoka.tournament.capi.dao.TournamentDao;
import de.hatoka.tournament.capi.entities.BlindLevelPO;
import de.hatoka.tournament.capi.entities.CompetitorPO;
import de.hatoka.tournament.capi.entities.HistoryPO;
import de.hatoka.tournament.capi.entities.PlayerPO;
import de.hatoka.tournament.capi.entities.RankPO;
import de.hatoka.tournament.capi.entities.TournamentPersistenceModel;
import de.hatoka.tournament.capi.entities.TournamentPO;

public class TournamentBORepositoryImpl implements TournamentBORepository
{
    private final String accountRef;

    private final TournamentBusinessFactory factory;
    private final TournamentDao tournamentDao;
    private final PlayerDao playerDao;
    private final CompetitorDao competitorDao;
    private final BlindLevelDao blindLevelDao;
    private final HistoryDao historyDao;
    private final RankDao rankDao;
    private final UUIDGenerator uuidGenerator;
    private final UUIDGenerator externalRefGenerator;

    private final TransactionProvider transactionProvider;

    public TournamentBORepositoryImpl(String accountRef, TournamentDao tournamentDao, PlayerDao playerDao,
                    CompetitorDao competitorDao, BlindLevelDao blindLevelDao, HistoryDao historyDao, RankDao rankDao, UUIDGenerator uuidGenerator, UUIDGenerator externalRefGenerator, TransactionProvider transactionProvider, TournamentBusinessFactory factory)
    {
        this.accountRef = accountRef;
        this.factory = factory;
        this.tournamentDao = tournamentDao;
        this.playerDao = playerDao;
        this.competitorDao = competitorDao;
        this.blindLevelDao = blindLevelDao;
        this.historyDao = historyDao;
        this.rankDao = rankDao;
        this.uuidGenerator = uuidGenerator;
        this.externalRefGenerator = externalRefGenerator;
        this.transactionProvider = transactionProvider;
    }

    @Override
    public TournamentBO createTournament(String name, Date date)
    {
        return createTournament(externalRefGenerator.generate(), name, date);
    }

    @Override
    public TournamentBO createTournament(String externalRef, String name, Date date)
    {
        TournamentPO tournamentPO = tournamentDao.createAndInsert(accountRef, externalRef, name, date, false);
        return getTournamentBO(tournamentPO);
    }

    private TournamentBO getTournamentBO(TournamentPO tournamentPO)
    {
        return factory.getTournamentBO(tournamentPO);
    }
    @Override
    public CashGameBO createCashGame(Date date)
    {
        return createCashGame(externalRefGenerator.generate(), date);
    }
    @Override
    public CashGameBO createCashGame(String externalRef, Date date)
    {
        String name = new SimpleDateFormat("yyyy/mm/dd hh:MM").format(date);
        TournamentPO tournamentPO = tournamentDao.createAndInsert(accountRef, externalRef, name, date, true);

        return getCashGameBO(tournamentPO);
    }

    private CashGameBO getCashGameBO(TournamentPO tournamentPO)
    {
        return factory.getCashGameBO(tournamentPO);
    }

    @Override
    public TournamentBO getTournamentByID(String id)
    {
        TournamentPO tournamentPO = tournamentDao.getById(id);
        if (!accountRef.equals(tournamentPO.getAccountRef()))
        {
            throw new IllegalArgumentException("tournament not assigned to account");
        }
        if (tournamentPO.isCashGame())
        {
            throw new IllegalArgumentException("requested tournament is a cash game");
        }
        return getTournamentBO(tournamentPO);
    }

    @Override
    public CashGameBO getCashGameByID(String id)
    {
        TournamentPO tournamentPO = tournamentDao.getById(id);
        if (!accountRef.equals(tournamentPO.getAccountRef()))
        {
            throw new IllegalArgumentException("cashgame not assigned to account");
        }
        if (!tournamentPO.isCashGame())
        {
            throw new IllegalArgumentException("requested cash game is a tournament");
        }
        return getCashGameBO(tournamentPO);
    }

    @Override
    public List<TournamentBO> getTournaments()
    {
        return tournamentDao.getByAccountRef(accountRef).stream().filter(tournamentPO -> !tournamentPO.isCashGame()).map(tournamentPO -> getTournamentBO(tournamentPO))
                        .collect(Collectors.toList());
    }

    @Override
    public List<CashGameBO> getCashGames()
    {
        return tournamentDao.getByAccountRef(accountRef).stream().filter(tournamentPO -> tournamentPO.isCashGame()).map(tournamentPO -> getCashGameBO(tournamentPO))
                        .collect(Collectors.toList());
    }

    @Override
    public void exportXML(Writer writer) throws JAXBException
    {
        TournamentPersistenceModel tournamentPersistenceModel = new TournamentPersistenceModel();
        tournamentPersistenceModel.setPlayerPOs(playerDao.getByAccountRef(accountRef));
        tournamentPersistenceModel.setTournamentPOs(tournamentDao.getByAccountRef(accountRef));

        // create JAXB context and instantiate marshaller
        JAXBContext context = JAXBContext.newInstance(TournamentPersistenceModel.class);
        Marshaller m = context.createMarshaller();
        m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
        m.marshal(tournamentPersistenceModel, writer);
    }

    @Override
    public List<Warning> importXML(InputStream inputStream) throws IOException
    {
        List<Warning> warnings = new ArrayList<>();
        TournamentPersistenceModel tournamentPersistenceModel = null;
        try
        {
            JAXBContext context = JAXBContext.newInstance(TournamentPersistenceModel.class);
            Unmarshaller um = context.createUnmarshaller();
            tournamentPersistenceModel = (TournamentPersistenceModel)um.unmarshal(inputStream);
        }
        catch(JAXBException e)
        {
            throw new IOException(e);
        }
        importPlayers(tournamentPersistenceModel);
        importTournaments(warnings, tournamentPersistenceModel);
        return warnings;
    }

    private void importTournaments(List<Warning> warnings, TournamentPersistenceModel tournamentPersistenceModel)
    {
        for (TournamentPO tournamentXML : tournamentPersistenceModel.getTournamentPOs())
        {
            TournamentPO tournamentPO = tournamentDao.findByExternalRef(accountRef, tournamentXML.getExternalRef());
            if (tournamentPO == null)
            {
                importTournament(tournamentXML);
            }
            else
            {
                warnings.add(Warning.valueOf("import.tournament.ignored", tournamentXML.getId()));
            }
        }
    }

    private void importTournament(TournamentPO tournamentXML)
    {
        transactionProvider.runInTransaction(() -> {
            // accountRef not stored at each element
            tournamentXML.setAccountRef(accountRef);
            tournamentXML.setId(uuidGenerator.generate());
            tournamentDao.insert(tournamentXML);
            importCompetitors(tournamentXML);
            importBindLevels(tournamentXML);
            importHistory(tournamentXML);
            importRanks(tournamentXML);
        });
    }

    private void importCompetitors(TournamentPO tournamentXML)
    {
        for (CompetitorPO competitorXML : tournamentXML.getCompetitors())
        {
            competitorXML.setId(uuidGenerator.generate());
            competitorXML.setAccountRef(accountRef);
            competitorXML.setTournamentPO(tournamentXML);
            //PlayerPO playerPO = playerDao.findByExternalRef(accountRef, competitorXML.getPlayerRef());
            //competitorXML.setPlayerPO(playerPO);
            competitorDao.insert(competitorXML);
        }
    }

    private void importBindLevels(TournamentPO tournamentXML)
    {
        int position = 0;
        for (BlindLevelPO blindLevelXML : tournamentXML.getBlindLevels())
        {
            blindLevelXML.setId(uuidGenerator.generate());
            blindLevelXML.setPosition(position++);
            blindLevelDao.insert(blindLevelXML);
        }
    }

    private void importRanks(TournamentPO tournamentXML)
    {
        for (RankPO rankXML : tournamentXML.getRanks())
        {
            rankXML.setId(uuidGenerator.generate());
            rankDao.insert(rankXML);
        }
    }

    private void importHistory(TournamentPO tournamentXML)
    {
        for (HistoryPO xml : tournamentXML.getHistoryEntries())
        {
            xml.setId(uuidGenerator.generate());
            historyDao.insert(xml);
        }
    }

    /**
     * Imports the players from given persistence model
     * @param tournamentPersistenceModel
     * @return map of external identifier to internal identifier
     */
    private void importPlayers(TournamentPersistenceModel tournamentPersistenceModel)
    {
        // remember stored id of player
        for (PlayerPO playerXML : tournamentPersistenceModel.getPlayerPOs())
        {
            PlayerPO playerPO = playerDao.findByExternalRef(accountRef, playerXML.getExternalRef());
            if (playerPO == null)
            {
                playerXML.setId(uuidGenerator.generate());
                playerXML.setAccountRef(accountRef);
                playerDao.insert(playerXML);
            }
            else
            {
                playerPO.setEMail(playerXML.getEMail());
            }
        }
    }
}
