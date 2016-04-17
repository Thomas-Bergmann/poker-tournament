package de.hatoka.tournament.internal.app.actions;

import java.net.URI;
import java.util.Date;
import java.util.List;

import de.hatoka.common.capi.app.model.SelectOptionVO;
import de.hatoka.tournament.capi.business.CompetitorBO;
import de.hatoka.tournament.capi.business.PlayerBO;
import de.hatoka.tournament.capi.business.TournamentBO;
import de.hatoka.tournament.capi.business.TournamentBusinessFactory;
import de.hatoka.tournament.internal.app.models.TournamentBigScreenModel;
import de.hatoka.tournament.internal.app.models.TournamentConfigurationModel;
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

    public TournamentBigScreenModel getTournamentBigScreenModel(Date currentTime)
    {
        return new TournamentBigScreenModel(getGame(), currentTime);
    }

}
