package de.hatoka.group.internal.business;

import java.util.Collection;
import java.util.stream.Collectors;

import de.hatoka.group.capi.business.GroupBO;
import de.hatoka.group.capi.business.GroupBusinessFactory;
import de.hatoka.group.capi.business.MemberBO;
import de.hatoka.group.capi.dao.GroupDao;
import de.hatoka.group.capi.dao.MemberDao;
import de.hatoka.group.capi.entities.GroupPO;
import de.hatoka.group.capi.entities.MemberPO;

public class GroupBOImpl implements GroupBO
{
    private GroupPO groupPO;
    private final GroupDao groupDao;
    private final MemberDao memberDao;
    private final GroupBusinessFactory factory;

    public GroupBOImpl(GroupPO groupPO, GroupDao groupDao,MemberDao memberDao, GroupBusinessFactory factory)
    {
        this.groupPO = groupPO;
        this.groupDao = groupDao;
        this.memberDao = memberDao;
        this.factory = factory;
    }

    @Override
    public String getOwner()
    {
        return groupPO.getOwnerRef();
    }

    @Override
    public void remove()
    {
        groupDao.remove(groupPO);
    }

    @Override
    public String getName()
    {
        return groupPO.getName();
    }

    @Override
    public MemberBO createMember(String userRef, String name)
    {
        MemberPO memberPO = memberDao.createAndInsert(groupPO, userRef, name);
        return factory.getMemberBO(memberPO);
    }

    @Override
    public Collection<MemberBO> getMembers()
    {
        return groupPO.getMembers().stream().map(po -> factory.getMemberBO(po)).collect(Collectors.toList());
    }
}
