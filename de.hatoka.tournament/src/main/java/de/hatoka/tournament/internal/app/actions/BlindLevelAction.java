package de.hatoka.tournament.internal.app.actions;

import java.net.URI;
import java.util.Iterator;
import java.util.List;

import de.hatoka.tournament.capi.business.PlayerBO;
import de.hatoka.tournament.capi.business.TournamentBO;
import de.hatoka.tournament.capi.business.TournamentBusinessFactory;
import de.hatoka.tournament.capi.business.TournamentRoundBO;
import de.hatoka.tournament.internal.app.models.BlindLevelVO;
import de.hatoka.tournament.internal.app.models.TournamentBlindLevelModel;
import de.hatoka.tournament.internal.app.models.TournamentVO;

public class BlindLevelAction
{
    private final TournamentBO tournamentBO;

    public BlindLevelAction(String accountRef, String tournamentID, TournamentBusinessFactory factory)
    {
        this.tournamentBO = factory.getTournamentBORepository(accountRef).getTournamentByID(tournamentID);
    }

    public TournamentBlindLevelModel getTournamentBlindLevelModel(URI tournamentURI)
    {
        TournamentBlindLevelModel model = new TournamentBlindLevelModel();
        model.setTournament(new TournamentVO(tournamentBO, tournamentURI));
        List<BlindLevelVO> blindLevels = model.getBlindLevels();
        for(TournamentRoundBO roundBO : tournamentBO.getBlindLevels())
        {
            blindLevels.add(new BlindLevelVO(roundBO));
        }
        model.getPrefilled().add(new BlindLevelVO());
        model.fillTime();
        return model;
    }

    public void deleteLevels(List<String> identifiers)
    {
        Iterator<TournamentRoundBO> itRounds = tournamentBO.getBlindLevels().iterator();
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
        tournamentBO.register(playerBO);
    }

    public void createPause(Integer duration)
    {
        tournamentBO.createPause(duration);
    }

    public void createBlindLevel(Integer duration, Integer smallBlind, Integer bigBlind, Integer ante)
    {
        tournamentBO.createBlindLevel(duration, smallBlind, bigBlind, ante);
    }

}
