package de.hatoka.tournament.internal.app.actions;

import java.net.URI;
import java.util.List;

import javax.ws.rs.core.UriBuilder;

import de.hatoka.tournament.capi.business.CompetitorBO;
import de.hatoka.tournament.capi.business.GameBO;
import de.hatoka.tournament.capi.business.HistoryEntryBO;
import de.hatoka.tournament.capi.business.PlayerBO;
import de.hatoka.tournament.capi.business.PlayerBORepository;
import de.hatoka.tournament.capi.business.TournamentBusinessFactory;
import de.hatoka.tournament.internal.app.models.CompetitorVO;
import de.hatoka.tournament.internal.app.models.HistoryEntryVO;
import de.hatoka.tournament.internal.app.models.HistoryModel;
import de.hatoka.tournament.internal.app.models.PlayerVO;
import de.hatoka.tournament.internal.app.models.TournamentPlayerListModel;
import de.hatoka.tournament.internal.app.models.TournamentVO;

/* package */ class GameAction<T extends GameBO>
{
    private final TournamentBusinessFactory factory;
    private final String accountRef;
    private final T gameBO;

    public GameAction(String accountRef, T gameBO, TournamentBusinessFactory factory)
    {
        this.accountRef = accountRef;
        this.gameBO = gameBO;
        this.factory = factory;
    }

    public void sortPlayers()
    {
        gameBO.sortCompetitors();
    }

    protected T getGame()
    {
        return gameBO;
    }

    public void unassignPlayers(List<String> identifiers)
    {
        for (CompetitorBO competitorBO : gameBO.getCompetitors())
        {
            if (identifiers.contains(competitorBO.getID()))
            {
                gameBO.unassign(competitorBO);
            }
        }
    }

    public HistoryModel getHistoryModel()
    {
        HistoryModel result = new HistoryModel();
        List<HistoryEntryVO> entries = result.getEntries();
        for (HistoryEntryBO historyBO : gameBO.getHistoryEntries())
        {
            entries.add(new HistoryEntryVO(historyBO));
        }
        return result;
    }

    public TournamentPlayerListModel getPlayerListModel(URI listTournamentURI, UriBuilder uriBuilder)
    {
        TournamentPlayerListModel result = new TournamentPlayerListModel();
        result.setTournament(new TournamentVO(gameBO, uriBuilder.build(gameBO.getID())));
        for (CompetitorBO competitor : gameBO.getCompetitors())
        {
            result.getCompetitors().add(new CompetitorVO(competitor));
        }
        PlayerBORepository playerBORepository = factory.getPlayerBORepository(accountRef);
        for (PlayerBO player : playerBORepository.getPlayers())
        {
            if (!gameBO.isCompetitor(player))
            {
                result.getUnassignedPlayers().add(new PlayerVO(player));
            }
        }
        return result;
    }
}
