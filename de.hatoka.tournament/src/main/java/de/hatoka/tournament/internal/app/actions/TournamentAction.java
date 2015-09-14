package de.hatoka.tournament.internal.app.actions;

import java.net.URI;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import de.hatoka.common.capi.app.model.ActionVO;
import de.hatoka.common.capi.app.model.SelectOptionVO;
import de.hatoka.tournament.capi.business.CompetitorBO;
import de.hatoka.tournament.capi.business.PlayerBO;
import de.hatoka.tournament.capi.business.TableBO;
import de.hatoka.tournament.capi.business.TournamentBO;
import de.hatoka.tournament.capi.business.TournamentBusinessFactory;
import de.hatoka.tournament.internal.app.models.CompetitorVO;
import de.hatoka.tournament.internal.app.models.TableVO;
import de.hatoka.tournament.internal.app.models.TournamentConfigurationModel;
import de.hatoka.tournament.internal.app.models.TournamentTableModel;
import de.hatoka.tournament.internal.app.models.TournamentVO;

public class TournamentAction extends GameAction<TournamentBO>
{
    public TournamentAction(String accountRef, String tournamentID, TournamentBusinessFactory factory)
    {
        super(accountRef, factory.getTournamentBORepository(accountRef).getTournamentByID(tournamentID), factory);
    }

    public void buyInPlayers(List<String> identifiers)
    {
        TournamentBO tournamentBO = getGame();
        for (CompetitorBO competitorBO : tournamentBO.getCompetitors())
        {
            if (identifiers.contains(competitorBO.getID()))
            {
                tournamentBO.buyin(competitorBO);
            }
        }
    }

    public void rebuyCompetitor(String competitorID)
    {
        TournamentBO tournamentBO = getGame();
        CompetitorBO competitorBO = tournamentBO.getCompetitorBO(competitorID);
        tournamentBO.rebuy(competitorBO);
    }

    public void seatOpenCompetitor(String competitorID)
    {
        TournamentBO tournamentBO = getGame();
        CompetitorBO competitorBO = tournamentBO.getCompetitorBO(competitorID);
        tournamentBO.seatOpen(competitorBO);
    }

    public CompetitorBO register(PlayerBO playerBO)
    {
        TournamentBO tournamentBO = getGame();
        return tournamentBO.register(playerBO);
    }

    public TournamentConfigurationModel getTournamentConfigurationModel(URI tournamentUri)
    {
        TournamentBO tournamentBO = getGame();
        TournamentConfigurationModel result = new TournamentConfigurationModel();
        TournamentVO tournamentVO = new TournamentVO(tournamentBO, tournamentUri);
        result.setTournament(tournamentVO);
        result.getReBuyOption().getOptions().add(new SelectOptionVO("multi", false));
        result.getReBuyOption().getOptions().add(new SelectOptionVO("single", true));
        result.getReBuyOption().getOptions().add(new SelectOptionVO("no", false));
        return result;
    }

    public TournamentTableModel getTournamentTableModel(URI tournamentUri, TournamentTableURIBuilder uriBuilder)
    {
        TournamentBO tournamentBO = getGame();
        TournamentTableModel result = new TournamentTableModel();
        TournamentVO tournamentVO = new TournamentVO(tournamentBO, tournamentUri);
        result.setTournament(tournamentVO);
        result.getTables().addAll(getTables(tournamentBO, uriBuilder));
        return result;
    }

    public static interface TournamentTableURIBuilder
    {
        URI getRebuyURI(String competitorID);

        URI getSeatOpenURI(String competitorID);
    }

    private Collection<? extends TableVO> getTables(TournamentBO tournamentBO, TournamentTableURIBuilder uriBuilder)
    {
        List<TableVO> result = new ArrayList<>();
        int tableNo = 1;
        for (TableBO tableBO : tournamentBO.getTables())
        {
            TableVO tableVO = new TableVO();
            tableVO.setNumber(tableNo++);
            final List<CompetitorVO> competitorVOs = tableVO.getCompetitors();
            for (CompetitorBO competitorBO : tableBO.getCompetitors())
            {
                final CompetitorVO competitorVO = new CompetitorVO(competitorBO);
                competitorVO.getActions().add(new ActionVO("reBuy", uriBuilder.getRebuyURI(competitorBO.getID()),"repeat"));
                competitorVO.getActions().add(new ActionVO("seatOpen", uriBuilder.getSeatOpenURI(competitorBO.getID()), "remove-circle"));
                competitorVOs.add(competitorVO);
            }
            result.add(tableVO);
        }
        return result;
    }
}
