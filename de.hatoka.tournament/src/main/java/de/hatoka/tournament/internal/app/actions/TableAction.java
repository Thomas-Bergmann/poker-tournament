package de.hatoka.tournament.internal.app.actions;

import java.net.URI;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import de.hatoka.common.capi.app.model.ActionVO;
import de.hatoka.tournament.capi.business.CompetitorBO;
import de.hatoka.tournament.capi.business.TableBO;
import de.hatoka.tournament.capi.business.TournamentBO;
import de.hatoka.tournament.capi.business.TournamentBusinessFactory;
import de.hatoka.tournament.internal.app.models.CompetitorVO;
import de.hatoka.tournament.internal.app.models.TableVO;
import de.hatoka.tournament.internal.app.models.TournamentTableModel;
import de.hatoka.tournament.internal.app.models.TournamentVO;

public class TableAction extends GameAction<TournamentBO>
{
    public TableAction(String accountRef, String tournamentID, TournamentBusinessFactory factory)
    {
        super(accountRef, factory.getTournamentBORepository(accountRef).getTournamentByID(tournamentID), factory);
    }

    public TournamentTableModel getTournamentTableModel(URI tournamentUri, TournamentTableURIBuilder uriBuilder)
    {
        TournamentBO tournamentBO = getGame();
        TournamentTableModel result = new TournamentTableModel();
        TournamentVO tournamentVO = new TournamentVO(tournamentBO, tournamentUri);
        result.setTournament(tournamentVO);
        result.getTables().addAll(getTables(tournamentBO, uriBuilder));
        result.setPlacedCompetitors(tournamentBO.getPlacedCompetitors().stream().map(bo -> new CompetitorVO(bo)).sorted(new Comparator<CompetitorVO>()
        {
            @Override
            public int compare(CompetitorVO o1, CompetitorVO o2)
            {
                return o1.getPosition().compareTo(o2.getPosition());
            }
        }).collect(Collectors.toList()));
        return result;
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
