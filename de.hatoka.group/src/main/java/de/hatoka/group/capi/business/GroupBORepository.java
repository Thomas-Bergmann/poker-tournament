package de.hatoka.group.capi.business;

import java.util.List;
import java.util.Optional;

import de.hatoka.player.capi.business.PlayerRef;
import de.hatoka.user.capi.business.UserRef;

public interface GroupBORepository
{
    /**
     * Creates a new group
     *
     * @param groupRef
     * @param groupName
     *            of group
     * @param initial owner and member
     * @param ownerMemberName
     *            name of owner in the group
     * @return created group
     */
    GroupBO createGroup(GroupRef groupRef, String name);

    /**
     * Creates a new group
     *
     * @param name of group
     * @return created group
     */
    Optional<GroupBO> findGroup(GroupRef groupRef);

    /**
     * @return list of all groups
     */
    List<GroupBO> getAllGroups();

    /**
     * @return list of related groups (admin)
     */
    List<GroupBO> getGroups(UserRef userRef);

    /**
     * @return list of related groups (player)
     */
    List<GroupBO> getGroups(PlayerRef memberRef);

    /**
     * Removes all groups of this repository
     */
    default void clear()
    {
        getAllGroups().forEach(GroupBO::remove);
    }
}
