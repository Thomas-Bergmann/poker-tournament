package de.hatoka.tournament.internal.app.actions;

import de.hatoka.tournament.capi.business.PlayerBO;
import de.hatoka.tournament.capi.business.PlayerBORepository;
import de.hatoka.tournament.capi.business.TournamentBusinessFactory;

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
        return playerBORepository.findByID(playerId);
    }
}
