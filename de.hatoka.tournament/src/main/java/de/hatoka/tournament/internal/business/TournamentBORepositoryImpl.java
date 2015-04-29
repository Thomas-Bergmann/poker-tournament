package de.hatoka.tournament.internal.business;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

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

    public TournamentBORepositoryImpl(String accountRef, TournamentDao tournamentDao, PlayerDao playerDao,
                    CompetitorDao competitorDao, TournamentBusinessFactory factory)
    {
        this.accountRef = accountRef;
        this.factory = factory;
        this.tournamentDao = tournamentDao;
        this.playerDao = playerDao;
        this.competitorDao = competitorDao;
    }

    @Override
    public TournamentBO createTournament(String name, Date date)
    {
        TournamentPO tournamentPO = tournamentDao.createAndInsert(accountRef, name, date, false);
        return getTournamentBO(tournamentPO);
    }

    private TournamentBO getTournamentBO(TournamentPO tournamentPO)
    {
        return factory.getTournamentBO(tournamentPO);
    }

    @Override
    public CashGameBO createCashGame(Date date)
    {
        String name = new SimpleDateFormat("yyyy/mm/dd hh:MM").format(date);
        TournamentPO tournamentPO = tournamentDao.createAndInsert(accountRef, name, date, true);

        return getCashGameBO(tournamentPO);
    }

    private CashGameBO getCashGameBO(TournamentPO tournamentPO)
    {
        return factory.getCashGameBO(tournamentPO);
    }

    @Override
    public CashGameBO getByID(String id)
    {
        TournamentPO tournamentPO = tournamentDao.getById(id);
        if (!accountRef.equals(tournamentPO.getAccountRef()))
        {
            throw new IllegalArgumentException("tournament not assigned to account");
        }
        return getCashGameBO(tournamentPO);
    }

    @Override
    public List<TournamentBO> getTournamenBOs()
    {
        return tournamentDao.getByAccountRef(accountRef).stream().filter(tournamentPO -> !tournamentPO.isCashGame()).map(tournamentPO -> getTournamentBO(tournamentPO))
                        .collect(Collectors.toList());
    }

    @Override
    public List<CashGameBO> getCashGameBOs()
    {
        return tournamentDao.getByAccountRef(accountRef).stream().filter(tournamentPO -> tournamentPO.isCashGame()).map(tournamentPO -> getCashGameBO(tournamentPO))
                        .collect(Collectors.toList());
    }

    @Override
    public File exportFile() throws IOException
    {
        TournamentModel tournamentModel = new TournamentModel();
        tournamentModel.setAccountRef(accountRef);
        tournamentModel.setPlayerPOs(playerDao.getByAccountRef(accountRef));
        tournamentModel.setTournamentPOs(tournamentDao.getByAccountRef(accountRef));
        try
        {
            // create JAXB context and instantiate marshaller
            JAXBContext context = JAXBContext.newInstance(TournamentModel.class);
            Marshaller m = context.createMarshaller();
            m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);

            // export to file
            File result = Files.createTempFile("TournamentRepositoryExport", ".xml").toFile();
            m.marshal(tournamentModel, result);
            return result;
        }
        catch(JAXBException e)
        {
            throw new IOException(e);
        }
    }

    public List<String> importFile(File importFile) throws IOException
    {
        List<String> warnings = new ArrayList<>();
        TournamentModel tournamentModel = null;
        try
        {
            JAXBContext context = JAXBContext.newInstance(TournamentModel.class);
            Unmarshaller um = context.createUnmarshaller();
            tournamentModel = (TournamentModel)um.unmarshal(new FileReader(importFile));
        }
        catch(JAXBException e)
        {
            throw new IOException(e);
        }
        if (!accountRef.equals(tournamentModel.getAccountRef()))
        {
            warnings.add("import to other account: (from '" + tournamentModel.getAccountRef() + "', to '" + accountRef
                            + ")");
        }
        // remember stored id of player
        Map<String, String> playerRefMap = new HashMap<>();
        for (PlayerPO playerXML : tournamentModel.getPlayerPOs())
        {
            PlayerPO playerPO = playerDao.getById(playerXML.getId());
            if (playerPO == null)
            {
                // accountRef not stored at each element
                playerXML.setAccountRef(accountRef);
                playerDao.insert(playerXML);
                playerRefMap.put(playerXML.getId(), playerXML.getId());
            }
            else
            {
                String name = playerXML.getName();
                playerPO = playerDao.findByName(accountRef, name);
                if (playerPO == null)
                {
                    playerPO = playerDao.createAndInsert(accountRef, name);
                }
                playerRefMap.put(playerXML.getId(), playerPO.getId());
            }
        }
        for (TournamentPO tournamentXML : tournamentModel.getTournamentPOs())
        {
            TournamentPO tournamentPO = tournamentDao.getById(tournamentXML.getId());
            if (tournamentPO == null)
            {
                // accountRef not stored at each element
                tournamentXML.setAccountRef(accountRef);
                tournamentDao.insert(tournamentXML);
                for (CompetitorPO competitorXML : tournamentXML.getCompetitors())
                {
                    CompetitorPO competitorPO = competitorDao.getById(competitorXML.getId());
                    if (competitorPO != null)
                    {
                        throw new IllegalArgumentException("competitor assign to other tournament '"
                                        + competitorXML.getId() + "'");
                    }
                    competitorDao.insert(competitorXML);
                }
            }
            else
            {
                warnings.add("ignore existing tournament '" + tournamentXML.getId() + "'");
            }
        }
        return warnings;
    }
}
