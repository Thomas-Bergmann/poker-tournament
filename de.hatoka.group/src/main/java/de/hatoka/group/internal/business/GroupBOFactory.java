package de.hatoka.group.internal.business;

import de.hatoka.group.capi.business.GroupBO;
import de.hatoka.group.internal.persistence.GroupPO;

public interface GroupBOFactory
{
    GroupBO get(GroupPO groupPO);
}
