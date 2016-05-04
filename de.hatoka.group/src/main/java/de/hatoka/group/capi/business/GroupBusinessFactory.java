package de.hatoka.group.capi.business;

import de.hatoka.group.capi.entities.GroupPO;
import de.hatoka.group.capi.entities.MemberPO;

public interface GroupBusinessFactory
{
    GroupBO getGroupBO(GroupPO groupPO);

    GroupBORepository getGroupBORepository(String ownerRef);

    MemberBO getMemberBO(MemberPO memberPO);
}
