package de.hatoka.group.capi.business;

import java.util.Collection;

import de.hatoka.player.capi.business.PlayerBO;
import de.hatoka.user.capi.business.UserBO;

public interface GroupBO
{
    /**
     * @return name of group
     */
    String getName();

    /**
     * Create member for given player
     * @param playerRef
     * @param name
     *            of member in side of the group
     * @return created member
     */
    GroupMemberBO createMember(PlayerBO player);

    /**
     * Creates a group admin (must be a user)
     * @param userRef
     * @return created admin user
     */
    GroupAdminBO createAdmin(UserBO user);

    /**
     * @return members of the group (players)
     */
    Collection<GroupMemberBO> getMembers();

    /**
     * @return admins of the group (users)
     */
    Collection<GroupAdminBO> getAdmins();

    /**
     * @return reference to the group
     */
    GroupRef getRef();

    /**
     * Removes that group
     */
    void remove();
}
