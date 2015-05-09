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

import de.hatoka.common.capi.dao.UUIDGenerator;
import de.hatoka.tournament.capi.business.CashGameBO;
import de.hatoka.tournament.capi.business.TournamentBO;
import de.hatoka.tournament.capi.business.TournamentBORepository;
import de.hatoka.tournament.capi.business.TournamentBusinessFactory;
import de.hatoka.tournament.capi.dao.CompetitorDao;
import de.hatoka.tournament.capi.dao.PlayerDao;
import de.hatoka.tournament.capi.dao.TournamentDao;
import de.hatoka.tournament.capi.entities.CompetitorPO;
import de.hatoka.tournament.capi.entities.PlayerPO;
import de.hatoka.tournament.capi.entities.TournamentModel;
import de.hatoka.tournament.capi.entities.TournamentPO;

public class TournamentBORepositoryImpl implements TournamentBORepository
{
    private final String accountRef;

    private final TournamentBusinessFactory factory;
    private final TournamentDao tournamentDao;
    private final PlayerDao playerDao;
    private final CompetitorDao competitorDao;
    private final UUIDGenerator uuidGenerator;
    private final UUIDGenerator externalRefGenerator;

    public TournamentBORepositoryImpl(String accountRef, TournamentDao tournamentDao, PlayerDao playerDao,
                    CompetitorDao competitorDao, UUIDGenerator uuidGenerator, UUIDGenerator externalRefGenerator, TournamentBusinessFactory factory)
    {
        this.accountRef = accountRef;
        this.factory = factory;
        this.tournamentDao = tournamentDao;
        this.playerDao = playerDao;
        this.competitorDao = competitorDao;
        this.uuidGenerator = uuidGenerator;
        this.externalRefGenerator = externalRefGenerator;
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
        TournamentModel tournamentModel = new TournamentModel();
        tournamentModel.setPlayerPOs(playerDao.getByAccountRef(accountRef));
        tournamentModel.setTournamentPOs(tournamentDao.getByAccountRef(accountRef));

        // create JAXB context and instantiate marshaller
        JAXBContext context = JAXBContext.newInstance(TournamentModel.class);
        Marshaller m = context.createMarshaller();
        m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
        m.marshal(tournamentModel, writer);
    }

    public List<String> importXML(InputStream inputStream) throws IOException
    {
        List<String> warnings = new ArrayList<>();
        TournamentModel tournamentModel = null;
        try
        {
            JAXBContext context = JAXBContext.newInstance(TournamentModel.class);
            Unmarshaller um = context.createUnmarshaller();
            tournamentModel = (TournamentModel)um.unmarshal(inputStream);
        }
        catch(JAXBException e)
        {
            throw new IOException(e);
        }
        importPlayers(tournamentModel);
        importTournaments(warnings, tournamentModel);
        return warnings;
    }

    private void importTournaments(List<String> warnings, TournamentModel tournamentModel)
    {
        for (TournamentPO tournamentXML : tournamentModel.getTournamentPOs())
        {
            TournamentPO tournamentPO = tournamentDao.findByExternalRef(accountRef, tournamentXML.getExternalRef());
            if (tournamentPO == null)
            {
                // accountRef not stored at each element
                tournamentXML.setAccountRef(accountRef);
                tournamentXML.setId(uuidGenerator.generate());
                tournamentDao.insert(tournamentXML);
                importCompetitors(tournamentXML);
            }
            else
            {
                warnings.add("ignore existing tournament '" + tournamentXML.getId() + "'");
            }
        }
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

    /**
     * Imports the players from given persistence model
     * @param tournamentModel
     * @return map of external identifier to internal identifier
     */
    private void importPlayers(TournamentModel tournamentModel)
    {
        // remember stored id of player
        for (PlayerPO playerXML : tournamentModel.getPlayerPOs())
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
