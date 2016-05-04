package de.hatoka.group.internal.business;

import java.util.List;
import java.util.stream.Collectors;

import de.hatoka.group.capi.business.GroupBO;
import de.hatoka.group.capi.business.GroupBORepository;
import de.hatoka.group.capi.business.GroupBusinessFactory;
import de.hatoka.group.capi.dao.GroupDao;
import de.hatoka.group.capi.dao.MemberDao;
import de.hatoka.group.capi.entities.GroupPO;

public class GroupBORepositoryImpl implements GroupBORepository
{
    private final String ownerRef;

    private final GroupBusinessFactory factory;
    private final GroupDao groupDao;

    public GroupBORepositoryImpl(String ownerRef, GroupDao groupDao, MemberDao memberDao, GroupBusinessFactory factory)
    {
        this.ownerRef = ownerRef;
        this.factory = factory;
        this.groupDao = groupDao;
    }

    @Override
    public GroupBO createGroup(String name, String ownerName)
    {
        GroupPO groupPO = groupDao.createAndInsert(ownerRef, name);
        GroupBO groupBO = factory.getGroupBO(groupPO);
        groupBO.createMember(ownerRef, ownerName);
        return groupBO;
    }

    @Override
    public List<GroupBO> getGroups()
    {
        return groupDao.getByOwner(ownerRef).stream().map(po -> factory.getGroupBO(po)).collect(Collectors.toList());
    }
}
