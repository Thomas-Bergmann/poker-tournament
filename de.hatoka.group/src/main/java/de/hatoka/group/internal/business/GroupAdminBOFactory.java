package de.hatoka.group.internal.business;

import de.hatoka.group.capi.business.GroupAdminBO;
import de.hatoka.group.capi.business.GroupBO;
import de.hatoka.group.internal.persistence.GroupAdminPO;

public interface GroupAdminBOFactory
{
    GroupAdminBO get(GroupBO group, GroupAdminPO adminPO);

}
