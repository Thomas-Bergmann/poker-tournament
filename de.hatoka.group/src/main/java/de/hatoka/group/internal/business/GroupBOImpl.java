package de.hatoka.group.internal.business;

import java.util.Collection;
import java.util.stream.Collectors;

import jakarta.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import de.hatoka.group.capi.business.GroupAdminBO;
import de.hatoka.group.capi.business.GroupBO;
import de.hatoka.group.capi.business.GroupMemberBO;
import de.hatoka.group.capi.business.GroupRef;
import de.hatoka.group.internal.persistence.GroupAdminDao;
import de.hatoka.group.internal.persistence.GroupAdminPO;
import de.hatoka.group.internal.persistence.GroupDao;
import de.hatoka.group.internal.persistence.GroupMemberDao;
import de.hatoka.group.internal.persistence.GroupMemberPO;
import de.hatoka.group.internal.persistence.GroupPO;
import de.hatoka.player.capi.business.PlayerBO;
import de.hatoka.user.capi.business.UserBO;

@Component
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class GroupBOImpl implements GroupBO
{
    @Autowired
    private GroupDao groupDao;
    @Autowired
    private GroupMemberDao memberDao;
    @Autowired
    private GroupMemberBOFactory memberFactory;
    @Autowired
    private GroupAdminDao adminDao;
    @Autowired
    private GroupAdminBOFactory adminFactory;

    private final GroupRef groupRef;
    private final GroupPO groupPO;

    public GroupBOImpl(GroupPO groupPO)
    {
        this.groupPO = groupPO;
        this.groupRef = GroupRef.valueOfGlobal(groupPO.getGlobalGroupRef());
    }

    @Override
    public String getName()
    {
        return groupPO.getName();
    }

    private GroupMemberBO getMember(GroupMemberPO memberPO)
    {
        return memberFactory.get(this, memberPO);
    }

    @Override
    @Transactional
    public GroupMemberBO createMember(PlayerBO player)
    {
        GroupMemberPO memberPO = new GroupMemberPO();
        memberPO.setGroupRef(groupRef.getGlobalRef());
        memberPO.setPlayerRef(player.getRef().getGlobalRef());
        return getMember(memberDao.save(memberPO));
    }

    private GroupAdminBO getAdmin(GroupAdminPO adminPO)
    {
        return adminFactory.get(this, adminPO);
    }

    @Override
    @Transactional
    public GroupAdminBO createAdmin(UserBO user)
    {
        GroupAdminPO adminPO = new GroupAdminPO();
        adminPO.setGroupRef(groupRef.getGlobalRef());
        adminPO.setUserRef(user.getRef().getGlobalRef());
        return getAdmin(adminDao.save(adminPO));
    }

    @Override
    public Collection<GroupMemberBO> getMembers()
    {
        return memberDao.findByGroupRef(groupRef.getGlobalRef()).stream().map(this::getMember).collect(Collectors.toList());
    }

    @Override
    public Collection<GroupAdminBO> getAdmins()
    {
        return adminDao.findByGroupRef(groupRef.getGlobalRef()).stream().map(this::getAdmin).collect(Collectors.toList());
    }

    @Override
    public GroupRef getRef()
    {
        return groupRef;
    }

    @Override
    public int hashCode()
    {
        return groupPO.hashCode();
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
        GroupBOImpl other = (GroupBOImpl)obj;
        if (groupPO == null)
        {
            if (other.groupPO != null)
                return false;
        }
        else if (!groupPO.equals(other.groupPO))
            return false;
        return true;
    }

    @Override
    @Transactional
    public void remove()
    {
        memberDao.deleteAllInBatch(memberDao.findByGroupRef(groupPO.getGlobalGroupRef()));
        adminDao.deleteAllInBatch(adminDao.findByGroupRef(groupPO.getGlobalGroupRef()));
        groupDao.delete(groupPO);
    }
}
