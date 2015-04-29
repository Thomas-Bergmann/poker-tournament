package de.hatoka.tournament.internal.app.actions;

import java.util.Collection;
import java.util.List;

import de.hatoka.common.capi.business.Money;
import de.hatoka.tournament.capi.business.CompetitorBO;
import de.hatoka.tournament.capi.business.TournamentBO;

public class TournamentAction
{
    private final TournamentBO tournamentBO;

    public TournamentAction(TournamentBO tournamentBO)
    {
        this.tournamentBO = tournamentBO;
    }

    public List<String> rebuyPlayers(List<String> identifiers)
    {
        Money rebuy = tournamentBO.getReBuy();
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
        return java.util.Collections.emptyList();
    }

    public void seatOpenPlayers(String identifier)
    {
        Collection<CompetitorBO> activeCompetitors = tournamentBO.getActiveCompetitors();
        for (CompetitorBO competitorBO : activeCompetitors)
        {
            if (identifier.equals(competitorBO.getID()))
            {
                tournamentBO.seatOpen(competitorBO);
            }
        }
    }

}
