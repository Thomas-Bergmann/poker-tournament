package de.hatoka.user.capi.business;

import de.hatoka.user.capi.entities.UserPO;

public interface UserBusinessFactory
{
    public UserBO getUserBO(UserPO userPO);

    public UserBORepository getUserBORepository();
}