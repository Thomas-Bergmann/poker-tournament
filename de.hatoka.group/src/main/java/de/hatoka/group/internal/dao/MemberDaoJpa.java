package de.hatoka.group.internal.dao;

import javax.inject.Inject;

import de.hatoka.common.capi.dao.GenericJPADao;
import de.hatoka.common.capi.dao.UUIDGenerator;
import de.hatoka.group.capi.dao.MemberDao;
import de.hatoka.group.capi.entities.GroupPO;
import de.hatoka.group.capi.entities.MemberPO;

public class MemberDaoJpa extends GenericJPADao<MemberPO> implements MemberDao
{
    @Inject
    private UUIDGenerator uuidGenerator;

    public MemberDaoJpa()
    {
        super(MemberPO.class);
    }

    @Override
    public MemberPO createAndInsert(GroupPO groupPO, String userRef, String name)
    {
        MemberPO memberPO = create();
        memberPO.setId(uuidGenerator.generate());
        memberPO.setGroup(groupPO);
        memberPO.setUserRef(userRef);
        memberPO.setName(name);
        // add relations
        groupPO.getMembers().add(memberPO);
        // insert
        insert(memberPO);
        return memberPO;
    }


    @Override
    public void remove(MemberPO memberPO)
    {
        // remove relations
        GroupPO group = memberPO.getGroup();
        if (group != null)
        {
            group.getMembers().remove(memberPO);
            memberPO.setGroup(null);
        }
        super.remove(memberPO);
    }
}
