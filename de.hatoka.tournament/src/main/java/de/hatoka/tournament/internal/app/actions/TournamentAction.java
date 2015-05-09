package de.hatoka.tournament.internal.app.actions;

import java.net.URI;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import de.hatoka.tournament.capi.business.CompetitorBO;
import de.hatoka.tournament.capi.business.PlayerBO;
import de.hatoka.tournament.capi.business.TournamentBO;
import de.hatoka.tournament.capi.business.TournamentBusinessFactory;
import de.hatoka.tournament.capi.business.TournamentRoundBO;
import de.hatoka.tournament.internal.app.models.BlindLevelVO;
import de.hatoka.tournament.internal.app.models.TournamentBlindLevelModel;
import de.hatoka.tournament.internal.app.models.TournamentVO;

public class TournamentAction extends GameAction<TournamentBO>
{
    public TournamentAction(String accountRef, TournamentBO tournamentBO, TournamentBusinessFactory factory)
    {
        super(accountRef, tournamentBO, factory);
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

    public TournamentBlindLevelModel getTournamentBlindLevelModel(URI tournamentURI)
    {
        TournamentBO tournamentBO = getGame();
        TournamentBlindLevelModel model = new TournamentBlindLevelModel();
        model.setTournament(new TournamentVO(tournamentBO, tournamentURI));
        List<BlindLevelVO> blindLevels = model.getBlindLevels();
        for(TournamentRoundBO roundBO : tournamentBO.getTournamentRounds())
        {
            blindLevels.add(new BlindLevelVO(roundBO));
        }
        model.getPrefilled().add(new BlindLevelVO());
        model.fillTime();
        return model;
    }

    public void deleteLevels(List<String> identifiers)
    {
        TournamentBO tournamentBO = getGame();
        Iterator<TournamentRoundBO> itRounds = tournamentBO.getTournamentRounds().iterator();
        while(itRounds.hasNext())
        {
            TournamentRoundBO round = itRounds.next();
            if (identifiers.contains(round.getID()))
            {
                tournamentBO.remove(round);
            }
        }
    }

    public void register(PlayerBO playerBO)
    {
        TournamentBO tournamentBO = getGame();
        tournamentBO.register(playerBO);
    }

}
