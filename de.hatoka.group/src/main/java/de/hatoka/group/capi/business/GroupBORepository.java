package de.hatoka.group.capi.business;

import java.util.List;

public interface GroupBORepository
{
    /**
     * Creates a new group
     * @param name of group
     * @param ownerName member name of owner for this group
     * @return created group
     */
    GroupBO createGroup(String name, String ownerName);

    /**
     * @return list of owned groups
     */
    List<GroupBO> getGroups();

    /**
     * Removes all groups of this repository
     */
    void clear();
}
