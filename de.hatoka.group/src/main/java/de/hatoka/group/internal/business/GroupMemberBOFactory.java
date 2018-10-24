package de.hatoka.group.internal.business;

import de.hatoka.group.capi.business.GroupBO;
import de.hatoka.group.capi.business.GroupMemberBO;
import de.hatoka.group.internal.persistence.GroupMemberPO;

public interface GroupMemberBOFactory
{
    GroupMemberBO get(GroupBO group, GroupMemberPO memberPO);
}
