package de.hatoka.group.internal.business;

import java.util.Optional;

import jakarta.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import de.hatoka.group.capi.business.GroupBO;
import de.hatoka.group.capi.business.GroupMemberBO;
import de.hatoka.group.internal.persistence.GroupMemberDao;
import de.hatoka.group.internal.persistence.GroupMemberPO;
import de.hatoka.player.capi.business.PlayerBO;
import de.hatoka.player.capi.business.PlayerBORepository;
import de.hatoka.player.capi.business.PlayerRef;

@Component
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class GroupMemberBOImpl implements GroupMemberBO
{
    @Autowired
    private GroupMemberDao memberDao;
    @Autowired
    private PlayerBORepository playerBORepository;

    private final GroupBO groupBO;
    private final GroupMemberPO memberPO;

    public GroupMemberBOImpl(GroupBO groupBO, GroupMemberPO memberPO)
    {
        this.groupBO = groupBO;
        this.memberPO = memberPO;
    }

    @Override
    public int hashCode()
    {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((memberPO == null) ? 0 : memberPO.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj)
    {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        GroupMemberBOImpl other = (GroupMemberBOImpl)obj;
        if (memberPO == null)
        {
            if (other.memberPO != null)
                return false;
        }
        else if (!memberPO.equals(other.memberPO))
            return false;
        return true;
    }

    @Override
    @Transactional
    public void remove()
    {
        if (groupBO.getMembers().size() < 2)
        {
            throw new IllegalStateException("Can't delete last member");
        }
        memberDao.delete(memberPO);
    }

    @Override
    public Optional<PlayerBO> getPlayer()
    {
        return playerBORepository.findPlayer(PlayerRef.valueOf(memberPO.getPlayerRef()));
    }

    @Override
    public GroupBO getGroup()
    {
        return groupBO;
    }
}
