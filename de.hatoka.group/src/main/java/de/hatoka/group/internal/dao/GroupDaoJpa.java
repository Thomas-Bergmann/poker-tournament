package de.hatoka.group.internal.dao;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import de.hatoka.common.capi.dao.GenericJPADao;
import de.hatoka.common.capi.dao.UUIDGenerator;
import de.hatoka.group.capi.dao.GroupDao;
import de.hatoka.group.capi.dao.MemberDao;
import de.hatoka.group.capi.entities.GroupPO;

public class GroupDaoJpa extends GenericJPADao<GroupPO> implements GroupDao
{
    @Inject
    private UUIDGenerator uuidGenerator;
    @Inject
    private MemberDao memberDao;

    public GroupDaoJpa()
    {
        super(GroupPO.class);
    }

    @Override
    public GroupPO createAndInsert(String ownerRef, String name)
    {
        GroupPO result = create();
        result.setId(uuidGenerator.generate());
        result.setOwnerRef(ownerRef);
        result.setName(name);
        insert(result);
        return result;
    }

    @Override
    public List<GroupPO> getByOwner(String ownerRef)
    {
        return createNamedQuery("GroupPO.findByOwnerRef").setParameter("ownerRef", ownerRef).getResultList();
    }

    @Override
    public void remove(GroupPO groupPO)
    {
        new ArrayList<>(groupPO.getMembers()).forEach(element -> memberDao.remove(element));
        super.remove(groupPO);
    }

    @Override
    public GroupPO findByName(String name)
    {
        return getOptionalResult(createNamedQuery("GroupPO.findByName").setParameter("name", name));
    }

}
