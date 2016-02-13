package de.hatoka.tournament.internal.app.actions;

import java.util.List;
import java.util.stream.Collectors;

import de.hatoka.tournament.capi.business.PlayerBO;
import de.hatoka.tournament.capi.business.PlayerBORepository;
import de.hatoka.tournament.capi.business.TournamentBusinessFactory;
import de.hatoka.tournament.internal.app.models.PlayerListModel;
import de.hatoka.tournament.internal.app.models.PlayerVO;

public class PlayerAction
{
    private final TournamentBusinessFactory factory;
    private final String accountRef;

    public PlayerAction(String accountRef, TournamentBusinessFactory factory)
    {
        this.accountRef = accountRef;
        this.factory = factory;
    }

    public PlayerBO createPlayer(String name)
    {
        PlayerBORepository playerBORepository = factory.getPlayerBORepository(accountRef);
        return playerBORepository.create(name);
    }


    public PlayerBO getPlayer(String playerId)
    {
        PlayerBORepository playerBORepository = factory.getPlayerBORepository(accountRef);
        return playerBORepository.getPlayerByID(playerId);
    }

    public PlayerListModel getPlayerListModel()
    {
        PlayerListModel result = new PlayerListModel();
        PlayerBORepository playerBORepository = factory.getPlayerBORepository(accountRef);
        result.setPlayers(playerBORepository.getPlayers().stream().map(bo -> new PlayerVO(bo)).collect(Collectors.toList()));
        return result;
    }

    public void deletePlayers(List<String> identifiers)
    {
        PlayerBORepository playerBORepository = factory.getPlayerBORepository(accountRef);
        for (String id : identifiers)
        {
            playerBORepository.getPlayerByID(id).remove();
        }
    }

}
