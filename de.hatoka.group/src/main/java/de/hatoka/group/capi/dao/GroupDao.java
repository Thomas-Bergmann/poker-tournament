package de.hatoka.group.capi.dao;

import java.util.Collection;

import de.hatoka.common.capi.dao.Dao;
import de.hatoka.group.capi.entities.GroupPO;

public interface GroupDao extends Dao<GroupPO>
{
    /**
     * @param owner userRef of owner
     * @param name
     *            name of group
     * @return
     */
    GroupPO createAndInsert(String ownerRef, String name);

    /**
     * @param owner
     *            userRef of owner
     * @return (optional)
     */
    Collection<GroupPO> getByOwner(String ownerRef);
}
