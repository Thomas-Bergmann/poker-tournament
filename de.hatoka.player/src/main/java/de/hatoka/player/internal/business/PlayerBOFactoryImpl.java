package de.hatoka.player.internal.business;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Lookup;
import org.springframework.stereotype.Component;

import de.hatoka.player.capi.business.PlayerBO;
import de.hatoka.player.capi.business.PlayerRef;
import de.hatoka.player.internal.persistence.PlayerDao;
import de.hatoka.player.internal.persistence.PlayerPO;

@Component
public class PlayerBOFactoryImpl implements PlayerBOFactory
{
    @Autowired
    private PlayerDao playerDao;

    @Lookup
    @Override
    public PlayerBO get(PlayerPO po)
    {
        // done by @Lookup
        return null;
    }

    @Override
    public Optional<PlayerBO> get(String ref)
    {
        PlayerRef playerRef = PlayerRef.valueOf(ref);
        return playerDao.findByContextRefAndPlayerRef(playerRef.getUserRef().getGlobalRef(), playerRef.getLocalRef()).map(this::get);
    }
}
