package de.hatoka.group.capi.dao;

import de.hatoka.common.capi.dao.Dao;
import de.hatoka.group.capi.entities.GroupPO;
import de.hatoka.group.capi.entities.MemberPO;

public interface MemberDao extends Dao<MemberPO>
{
    /**
     * Add member to group
     * @param groupPO
     * @param userRef
     * @return
     */
    public MemberPO createAndInsert(GroupPO groupPO, String userRef, String name);
}
