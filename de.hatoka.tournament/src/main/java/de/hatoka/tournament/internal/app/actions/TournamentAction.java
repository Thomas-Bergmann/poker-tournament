package de.hatoka.tournament.internal.app.actions;

import java.util.Collection;
import java.util.List;

import de.hatoka.tournament.capi.business.CompetitorBO;
import de.hatoka.tournament.capi.business.PlayerBO;
import de.hatoka.tournament.capi.business.TournamentBO;
import de.hatoka.tournament.capi.business.TournamentBusinessFactory;

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

    public void register(PlayerBO playerBO)
    {
        TournamentBO tournamentBO = getGame();
        tournamentBO.register(playerBO);
    }
}
