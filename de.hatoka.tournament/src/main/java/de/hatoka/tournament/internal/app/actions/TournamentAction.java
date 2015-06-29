package de.hatoka.tournament.internal.app.actions;

import java.net.URI;
import java.util.Collection;
import java.util.List;

import de.hatoka.common.capi.app.model.SelectOptionVO;
import de.hatoka.tournament.capi.business.CompetitorBO;
import de.hatoka.tournament.capi.business.PlayerBO;
import de.hatoka.tournament.capi.business.TournamentBO;
import de.hatoka.tournament.capi.business.TournamentBusinessFactory;
import de.hatoka.tournament.internal.app.models.TournamentConfigurationModel;
import de.hatoka.tournament.internal.app.models.TournamentVO;

public class TournamentAction extends GameAction<TournamentBO>
{
    public TournamentAction(String accountRef, String tournamentID, TournamentBusinessFactory factory)
    {
        super(accountRef, factory.getTournamentBORepository(accountRef).getTournamentByID(tournamentID), factory);
    }

    public List<String> buyInPlayers(List<String> identifiers)
    {
        TournamentBO tournamentBO = getGame();
        for (CompetitorBO competitorBO : tournamentBO .getCompetitors())
        {
            if (identifiers.contains(competitorBO.getID()))
            {
                tournamentBO.buyin(competitorBO);
            }
        }
        return java.util.Collections.emptyList();
    }


    public List<String> rebuyPlayers(List<String> identifiers)
    {
        TournamentBO tournamentBO = getGame();
        for (CompetitorBO competitorBO : tournamentBO .getCompetitors())
        {
            if (identifiers.contains(competitorBO.getID()))
            {
                tournamentBO.rebuy(competitorBO);
            }
        }
        return java.util.Collections.emptyList();
    }

    public void seatOpenPlayers(String identifier)
    {
        TournamentBO tournamentBO = getGame();
        Collection<CompetitorBO> activeCompetitors = tournamentBO.getActiveCompetitors();
        for (CompetitorBO competitorBO : activeCompetitors)
        {
            if (identifier.equals(competitorBO.getID()))
            {
                tournamentBO.seatOpen(competitorBO);
            }
        }
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
        tournamentVO.getReBuyOption().getOptions().add(new SelectOptionVO("multi", false));
        tournamentVO.getReBuyOption().getOptions().add(new SelectOptionVO("single", true));
        tournamentVO.getReBuyOption().getOptions().add(new SelectOptionVO("no", false));
        return result;
    }
}
