package de.hatoka.group.capi.business;

import java.util.List;

import de.hatoka.group.capi.entities.GroupPO;
import de.hatoka.group.capi.entities.MemberPO;

public interface GroupBusinessFactory
{
    GroupBO findGroupBOByName(String name);

    GroupBO getGroupBO(GroupPO groupPO);

    GroupBORepository getGroupBORepository(String ownerRef);

    MemberBO getMemberBO(MemberPO memberPO);

    List<GroupBO> getGroupBOsByUser(String userRef);
}
