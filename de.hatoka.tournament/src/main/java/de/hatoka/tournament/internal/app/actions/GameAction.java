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

public class GameAction
{
    private final TournamentBusinessFactory factory;
    private final String accountRef;
    private final GameBO gameBO;

    public GameAction(String accountRef, GameBO gameBO, TournamentBusinessFactory factory)
    {
        this.accountRef = accountRef;
        this.gameBO = gameBO;
        this.factory = factory;
    }

    public void sortPlayers()
    {
        gameBO.sortCompetitors();
    }

    public void assignPlayer(String playerID)
    {
        PlayerBORepository playerBORepository = factory.getPlayerBORepository(accountRef);
        PlayerBO playerBO = playerBORepository.getByID(playerID);
        if (playerBO != null)
        {
            CompetitorBO competitorBO = gameBO.assign(playerBO);
            competitorBO.buyin(gameBO.getBuyIn());
        }
    }

    public void createPlayer(String name)
    {
        PlayerBORepository playerBORepository = factory.getPlayerBORepository(accountRef);
        PlayerBO playerBO = playerBORepository.create(name);
        CompetitorBO competitorBO = gameBO.assign(playerBO);
        competitorBO.buyin(gameBO.getBuyIn());
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
        for (PlayerBO player : playerBORepository.getPlayerBOs())
        {
            if (!gameBO.isCompetitor(player))
            {
                result.getUnassignedPlayers().add(new PlayerVO(player));
            }
        }
        return result;
    }
}
