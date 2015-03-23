package de.hatoka.tournament.internal.app.actions;

import java.net.URI;
import java.util.Collection;
import java.util.List;

import javax.inject.Inject;
import javax.ws.rs.core.UriBuilder;

import de.hatoka.common.capi.business.Money;
import de.hatoka.tournament.capi.business.CompetitorBO;
import de.hatoka.tournament.capi.business.HistoryEntryBO;
import de.hatoka.tournament.capi.business.PlayerBO;
import de.hatoka.tournament.capi.business.PlayerBORepository;
import de.hatoka.tournament.capi.business.TournamentBO;
import de.hatoka.tournament.capi.business.TournamentBORepository;
import de.hatoka.tournament.capi.business.TournamentBusinessFactory;
import de.hatoka.tournament.internal.app.models.CompetitorVO;
import de.hatoka.tournament.internal.app.models.HistoryEntryVO;
import de.hatoka.tournament.internal.app.models.HistoryModel;
import de.hatoka.tournament.internal.app.models.PlayerVO;
import de.hatoka.tournament.internal.app.models.TournamentPlayerListModel;
import de.hatoka.tournament.internal.app.models.TournamentVO;

public class TournamentAction
{
    @Inject
    private TournamentBusinessFactory factory;

    private final String accountRef;

    public TournamentAction(String accountRef)
    {
        this.accountRef = accountRef;
    }

    public void assignPlayer(String tournamentID, String playerID)
    {
        TournamentBO tournamentBO = getTournamentBO(tournamentID);
        PlayerBORepository playerBORepository = factory.getPlayerBORepository(accountRef);
        PlayerBO playerBO = playerBORepository.getByID(playerID);
        if (playerBO != null)
        {
            CompetitorBO competitorBO = tournamentBO.assign(playerBO);
            competitorBO.buyin(tournamentBO.getBuyIn());
        }
    }

    public void createPlayer(String tournamentID, String name)
    {
        TournamentBO tournamentBO = getTournamentBO(tournamentID);
        PlayerBORepository playerBORepository = factory.getPlayerBORepository(accountRef);
        PlayerBO playerBO = playerBORepository.create(name);
        CompetitorBO competitorBO = tournamentBO.assign(playerBO);
        competitorBO.buyin(tournamentBO.getBuyIn());
    }

    public TournamentPlayerListModel getPlayerListModel(String tournamentID, URI listTournamentURI, UriBuilder uriBuilder)
    {
        TournamentBO tournamentBO = getTournamentBO(tournamentID);
        TournamentPlayerListModel result = new TournamentPlayerListModel();
        result.setTournament(new TournamentVO(tournamentBO, uriBuilder.build(tournamentBO.getID())));
        for (CompetitorBO competitor : tournamentBO.getCompetitors())
        {
            result.getCompetitors().add(new CompetitorVO(competitor));
        }
        PlayerBORepository playerBORepository = factory.getPlayerBORepository(accountRef);
        for (PlayerBO player : playerBORepository.getPlayerBOs())
        {
            if (!tournamentBO.isCompetitor(player))
            {
                result.getUnassignedPlayers().add(new PlayerVO(player));
            }
        }
        return result;
    }

    public void rebuyPlayers(String tournamentID, List<String> identifiers, String rebuyString)
    {
        TournamentBO tournamentBO = getTournamentBO(tournamentID);
        Money rebuy = Money.getInstance(rebuyString, tournamentBO.getSumInplay().getCurrency());
        for (CompetitorBO competitorBO : tournamentBO.getCompetitors())
        {
            if (identifiers.contains(competitorBO.getID()))
            {
                if (competitorBO.isActive())
                {
                    competitorBO.rebuy(rebuy);
                }
                else
                {
                    competitorBO.buyin(rebuy);
                }
            }
        }
    }

    public void seatOpenPlayers(String tournamentID, List<String> identifiers, String restAmountString)
    {
        TournamentBO tournamentBO = getTournamentBO(tournamentID);
        Collection<CompetitorBO> activeCompetitors = tournamentBO.getActiveCompetitors();
        Money restAmount = activeCompetitors.size() == identifiers.size() ? tournamentBO.getSumInplay().divide(identifiers.size()) : Money.getInstance(restAmountString, tournamentBO.getSumInplay().getCurrency());
        for (CompetitorBO competitorBO : activeCompetitors)
        {
            if (identifiers.contains(competitorBO.getID()))
            {
                competitorBO.seatOpen(restAmount);
            }
        }
    }

    public void unassignPlayers(String tournamentID, List<String> identifiers)
    {
        TournamentBO tournamentBO = getTournamentBO(tournamentID);
        for (CompetitorBO competitorBO : tournamentBO.getCompetitors())
        {
            if (identifiers.contains(competitorBO.getID()))
            {
                tournamentBO.unassign(competitorBO);
            }
        }
    }

    public void sortPlayers(String tournamentID)
    {
        TournamentBO tournamentBO = getTournamentBO(tournamentID);
        tournamentBO.sortCompetitors();
    }

    private TournamentBO getTournamentBO(String tournamentID)
    {
        TournamentBORepository tournamentBORepository = factory.getTournamentBORepository(accountRef);
        return tournamentBORepository.getByID(tournamentID);
    }

    public HistoryModel getHistoryModel(String tournamentID)
    {
        HistoryModel result = new HistoryModel();
        List<HistoryEntryVO> entries = result.getEntries();
        for(HistoryEntryBO historyBO : getTournamentBO(tournamentID).getHistoryEntries())
        {
            entries.add(new HistoryEntryVO(historyBO));
        }
        return result;
    }

}
