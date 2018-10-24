package de.hatoka.group.internal.business;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import jakarta.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import de.hatoka.group.capi.business.GroupBO;
import de.hatoka.group.capi.business.GroupBORepository;
import de.hatoka.group.capi.business.GroupRef;
import de.hatoka.group.internal.persistence.GroupAdminDao;
import de.hatoka.group.internal.persistence.GroupAdminPO;
import de.hatoka.group.internal.persistence.GroupDao;
import de.hatoka.group.internal.persistence.GroupMemberDao;
import de.hatoka.group.internal.persistence.GroupMemberPO;
import de.hatoka.group.internal.persistence.GroupPO;
import de.hatoka.player.capi.business.PlayerRef;
import de.hatoka.user.capi.business.UserRef;

@Component
public class GroupBORepositoryImpl implements GroupBORepository
{
    @Autowired
    private GroupDao groupDao;
    @Autowired
    private GroupMemberDao memberDao;
    @Autowired
    private GroupAdminDao adminDao;

    @Autowired
    private GroupBOFactory factory;

    @Override
    @Transactional
    public GroupBO createGroup(GroupRef groupRef, String name)
    {
        GroupPO groupPO = new GroupPO();
        groupPO.setGlobalGroupRef(groupRef.getGlobalRef());
        groupPO.setName(name);
        return factory.get(groupDao.save(groupPO));
    }

    @Override
    public List<GroupBO> getAllGroups()
    {
        return groupDao.findAll().stream().map(factory::get).collect(Collectors.toList());
    }

    @Override
    public List<GroupBO> getGroups(PlayerRef playerRef)
    {
        return memberDao.findByPlayerRef(playerRef.getGlobalRef())
                        .stream()
                        .map(GroupMemberPO::getGroupRef)
                        .map(groupDao::findByGlobalGroupRef)
                        .filter(Optional::isPresent)
                        .map(Optional::get)
                        .map(factory::get).collect(Collectors.toList());
    }

    @Override
    public List<GroupBO> getGroups(UserRef userRef)
    {
        return adminDao.findByUserRef(userRef.getGlobalRef())
                        .stream()
                        .map(GroupAdminPO::getGroupRef)
                        .map(groupDao::findByGlobalGroupRef)
                        .filter(Optional::isPresent)
                        .map(Optional::get)
                        .map(factory::get).collect(Collectors.toList());
    }

    @Override
    public Optional<GroupBO> findGroup(GroupRef groupRef)
    {
        return groupDao.findByGlobalGroupRef(groupRef.getGlobalRef()).map(factory::get);
    }
}
