package de.hatoka.player.internal.remote;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import de.hatoka.player.capi.business.PlayerBO;
import de.hatoka.player.capi.remote.PlayerDataRO;
import de.hatoka.player.capi.remote.PlayerInfoRO;
import de.hatoka.player.capi.remote.PlayerRO;

@Component
public class PlayerBO2RO
{
    public PlayerRO apply(PlayerBO player)
    {
        PlayerDataRO data = new PlayerDataRO();
        data.setName(player.getName());
        data.seteMail(player.getEMail());
        PlayerInfoRO info = new PlayerInfoRO();
        info.setFirstDate(player.getFirstDate());
        PlayerRO result = new PlayerRO();
        result.setRefGlobal(player.getRef().getGlobalRef());
        result.setRefLocal(player.getRef().getLocalRef());
        result.setData(data);
        result.setInfo(info);
        return result;
    }

    public List<PlayerRO> apply(List<PlayerBO> players)
    {
        return players.stream().map(this::apply).collect(Collectors.toList());
    }
}
