package de.hatoka.player.internal.business;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import de.hatoka.common.capi.configuration.DateProvider;
import de.hatoka.player.capi.business.PlayerBO;
import de.hatoka.player.capi.business.PlayerBORepository;
import de.hatoka.player.capi.business.PlayerRef;
import de.hatoka.player.internal.persistence.PlayerDao;
import de.hatoka.player.internal.persistence.PlayerPO;
import de.hatoka.user.capi.business.UserRef;

@Component
public class PlayerBORepositoryImpl implements PlayerBORepository
{
    @Autowired
    private PlayerDao playerDao;
    @Autowired
    private PlayerBOFactory playerBOFactory;
    @Autowired
    private DateProvider dateProvider;

    @Override
    public PlayerBO createPlayer(PlayerRef playerRef, String name)
    {
        PlayerPO po = new PlayerPO();
        po.setContextRef(playerRef.getUserRef().getGlobalRef());
        po.setPlayerRef(playerRef.getLocalRef());
        po.setName(name);
        po.setFirstDate(dateProvider.get());
        return playerBOFactory.get(playerDao.save(po));
    }

    @Override
    public Optional<PlayerBO> findPlayer(PlayerRef playerRef)
    {
        return playerDao.findByContextRefAndPlayerRef(playerRef.getUserRef().getGlobalRef(), playerRef.getLocalRef())
                        .map(playerBOFactory::get);
    }

    @Override
    public List<PlayerBO> getPlayers(UserRef userRef)
    {
        return playerDao.getByContextRef(userRef.getGlobalRef()).stream().map(playerBOFactory::get)
                        .collect(Collectors.toList());
    }

    @Override
    public Collection<PlayerBO> getAllPlayers()
    {
        return playerDao.findAll().stream().map(playerBOFactory::get).collect(Collectors.toList());
    }
}
