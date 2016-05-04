package de.hatoka.group.internal.business;

import javax.inject.Inject;

import de.hatoka.group.capi.business.GroupBO;
import de.hatoka.group.capi.business.GroupBORepository;
import de.hatoka.group.capi.business.GroupBusinessFactory;
import de.hatoka.group.capi.business.MemberBO;
import de.hatoka.group.capi.dao.GroupDao;
import de.hatoka.group.capi.dao.MemberDao;
import de.hatoka.group.capi.entities.GroupPO;
import de.hatoka.group.capi.entities.MemberPO;

public class GroupBusinessFactoryImpl implements GroupBusinessFactory
{
    @Inject
    private GroupDao groupDao;

    @Inject
    private MemberDao memberDao;

    @Override
    public GroupBORepository getGroupBORepository(String accountRef)
    {
        return new GroupBORepositoryImpl(accountRef, groupDao, memberDao, this);
    }

    @Override
    public GroupBO getGroupBO(GroupPO groupPO)
    {
        return new GroupBOImpl(groupPO, groupDao, memberDao, this);
    }

    @Override
    public MemberBO getMemberBO(MemberPO memberPO)
    {
        return new MemberBOImpl(memberPO, memberDao);
    }

}
