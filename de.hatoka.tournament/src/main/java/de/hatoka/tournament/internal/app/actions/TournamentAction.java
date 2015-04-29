package de.hatoka.tournament.internal.app.actions;

import java.net.URI;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import javax.inject.Inject;
import javax.ws.rs.core.UriBuilder;

import de.hatoka.common.capi.business.Money;
import de.hatoka.tournament.capi.business.CompetitorBO;
import de.hatoka.tournament.capi.business.HistoryEntryBO;
import de.hatoka.tournament.capi.business.PlayerBO;
import de.hatoka.tournament.capi.business.PlayerBORepository;
import de.hatoka.tournament.capi.business.CashGameBO;
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
        CashGameBO cashGameBO = getTournamentBO(tournamentID);
        PlayerBORepository playerBORepository = factory.getPlayerBORepository(accountRef);
        PlayerBO playerBO = playerBORepository.getByID(playerID);
        if (playerBO != null)
        {
            CompetitorBO competitorBO = cashGameBO.assign(playerBO);
            competitorBO.buyin(cashGameBO.getBuyIn());
        }
    }

    public void createPlayer(String tournamentID, String name)
    {
        CashGameBO cashGameBO = getTournamentBO(tournamentID);
        PlayerBORepository playerBORepository = factory.getPlayerBORepository(accountRef);
        PlayerBO playerBO = playerBORepository.create(name);
        CompetitorBO competitorBO = cashGameBO.assign(playerBO);
        competitorBO.buyin(cashGameBO.getBuyIn());
    }

    public TournamentPlayerListModel getPlayerListModel(String tournamentID, URI listTournamentURI,
                    UriBuilder uriBuilder)
    {
        CashGameBO cashGameBO = getTournamentBO(tournamentID);
        TournamentPlayerListModel result = new TournamentPlayerListModel();
        result.setTournament(new TournamentVO(cashGameBO, uriBuilder.build(cashGameBO.getID())));
        for (CompetitorBO competitor : cashGameBO.getCompetitors())
        {
            result.getCompetitors().add(new CompetitorVO(competitor));
        }
        PlayerBORepository playerBORepository = factory.getPlayerBORepository(accountRef);
        for (PlayerBO player : playerBORepository.getPlayerBOs())
        {
            if (!cashGameBO.isCompetitor(player))
            {
                result.getUnassignedPlayers().add(new PlayerVO(player));
            }
        }
        return result;
    }

    public List<String> rebuyPlayers(String tournamentID, List<String> identifiers, String rebuyString)
    {
        CashGameBO cashGameBO = getTournamentBO(tournamentID);
        Money rebuy = null;
        try
        {
            rebuy = Money.getInstance(rebuyString, cashGameBO.getSumInplay().getCurrency());
        } catch (NumberFormatException e)
        {
            return new ArrayList<String>(Arrays.asList("error.number.format"));
        }

        for (CompetitorBO competitorBO : cashGameBO.getCompetitors())
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
        return java.util.Collections.emptyList();
    }

    public void seatOpenPlayers(String tournamentID, List<String> identifiers, String restAmountString)
    {
        CashGameBO cashGameBO = getTournamentBO(tournamentID);
        Collection<CompetitorBO> activeCompetitors = cashGameBO.getActiveCompetitors();
        Money restAmount = activeCompetitors.size() == identifiers.size() ? cashGameBO.getSumInplay().divide(
                        identifiers.size()) : Money.getInstance(restAmountString, cashGameBO.getSumInplay()
                                        .getCurrency());
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
        CashGameBO cashGameBO = getTournamentBO(tournamentID);
        for (CompetitorBO competitorBO : cashGameBO.getCompetitors())
        {
            if (identifiers.contains(competitorBO.getID()))
            {
                cashGameBO.unassign(competitorBO);
            }
        }
    }

    public void sortPlayers(String tournamentID)
    {
        CashGameBO cashGameBO = getTournamentBO(tournamentID);
        cashGameBO.sortCompetitors();
    }

    private CashGameBO getTournamentBO(String tournamentID)
    {
        TournamentBORepository tournamentBORepository = factory.getTournamentBORepository(accountRef);
        return tournamentBORepository.getByID(tournamentID);
    }

    public HistoryModel getHistoryModel(String tournamentID)
    {
        HistoryModel result = new HistoryModel();
        List<HistoryEntryVO> entries = result.getEntries();
        for (HistoryEntryBO historyBO : getTournamentBO(tournamentID).getHistoryEntries())
        {
            entries.add(new HistoryEntryVO(historyBO));
        }
        return result;
    }

}
