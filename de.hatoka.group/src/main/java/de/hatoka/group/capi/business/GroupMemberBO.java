package de.hatoka.group.capi.business;

import java.util.Optional;

import de.hatoka.player.capi.business.PlayerBO;

public interface GroupMemberBO
{
    Optional<PlayerBO> getPlayer();
    GroupBO getGroup();
    void remove();
}
