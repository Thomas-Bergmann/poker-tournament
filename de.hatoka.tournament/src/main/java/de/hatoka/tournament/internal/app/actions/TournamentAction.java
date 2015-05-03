package de.hatoka.tournament.internal.app.actions;

import java.net.URI;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import de.hatoka.common.capi.business.Money;
import de.hatoka.tournament.capi.business.CompetitorBO;
import de.hatoka.tournament.capi.business.TournamentBO;
import de.hatoka.tournament.capi.business.TournamentRoundBO;
import de.hatoka.tournament.internal.app.models.BlindLevelVO;
import de.hatoka.tournament.internal.app.models.TournamentBlindLevelModel;
import de.hatoka.tournament.internal.app.models.TournamentVO;

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

    public TournamentBlindLevelModel getTournamentBlindLevelModel(URI tournamentURI)
    {
        TournamentBlindLevelModel model = new TournamentBlindLevelModel();
        model.setTournament(new TournamentVO(tournamentBO, tournamentURI));
        List<BlindLevelVO> blindLevels = model.getBlindLevels();
        for(TournamentRoundBO roundBO : tournamentBO.getTournamentRoundBOs())
        {
            blindLevels.add(new BlindLevelVO(roundBO));
        }
        model.getPrefilled().add(new BlindLevelVO());
        model.fillTime();
        return model;
    }

    public void deleteLevels(List<String> identifiers)
    {
        Iterator<TournamentRoundBO> itRounds = tournamentBO.getTournamentRoundBOs().iterator();
        while(itRounds.hasNext())
        {
            TournamentRoundBO round = itRounds.next();
            if (identifiers.contains(round.getID()))
            {
                tournamentBO.remove(round);
            }
        }
    }

}
