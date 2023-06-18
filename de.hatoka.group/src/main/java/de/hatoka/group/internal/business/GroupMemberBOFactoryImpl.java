package de.hatoka.group.internal.business;

import org.springframework.beans.factory.annotation.Lookup;
import org.springframework.stereotype.Component;

import de.hatoka.group.capi.business.GroupBO;
import de.hatoka.group.capi.business.GroupMemberBO;
import de.hatoka.group.internal.persistence.GroupMemberPO;

@Component
public class GroupMemberBOFactoryImpl implements GroupMemberBOFactory
{
    @Lookup
    @Override
    public GroupMemberBO get(GroupBO groupBO, GroupMemberPO memberPO)
    {
        // done by spring framework
        return null;
    }
}
