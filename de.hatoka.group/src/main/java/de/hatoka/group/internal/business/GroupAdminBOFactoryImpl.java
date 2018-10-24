package de.hatoka.group.internal.business;

import org.springframework.beans.factory.annotation.Lookup;
import org.springframework.stereotype.Component;

import de.hatoka.group.capi.business.GroupAdminBO;
import de.hatoka.group.capi.business.GroupBO;
import de.hatoka.group.internal.persistence.GroupAdminPO;

@Component
public class GroupAdminBOFactoryImpl implements GroupAdminBOFactory
{
    @Lookup
    @Override
    public GroupAdminBO get(GroupBO groupBO, GroupAdminPO adminPO)
    {
        // done by spring framework
        return null;
    }
}
