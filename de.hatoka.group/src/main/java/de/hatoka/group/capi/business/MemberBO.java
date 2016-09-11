package de.hatoka.group.capi.business;

public interface MemberBO
{
    String getUserRef();

    String getName();

    void remove();

    /**
     * @return identifier of membership
     */
    String getID();

    /**
     * @return true in case this member is the owner of the group
     */
    boolean isOwner();
}
