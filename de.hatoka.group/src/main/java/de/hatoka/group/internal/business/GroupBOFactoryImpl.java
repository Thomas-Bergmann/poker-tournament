package de.hatoka.group.internal.business;

import org.springframework.beans.factory.annotation.Lookup;
import org.springframework.stereotype.Component;

import de.hatoka.group.capi.business.GroupBO;
import de.hatoka.group.internal.persistence.GroupPO;

@Component
public class GroupBOFactoryImpl implements GroupBOFactory
{
    @Lookup
    @Override
    public GroupBO get(GroupPO groupPO)
    {
        // done by @Lookup
        return null;
    }
}
