package de.hatoka.player.internal.business;

import java.util.Optional;

import de.hatoka.player.capi.business.PlayerBO;
import de.hatoka.player.internal.persistence.PlayerPO;

public interface PlayerBOFactory
{
    PlayerBO get(PlayerPO po);
    Optional<PlayerBO> get(String ref);
}
