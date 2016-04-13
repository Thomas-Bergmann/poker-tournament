package de.hatoka.account.capi.business;

import de.hatoka.account.capi.entities.UserPO;

public interface UserBusinessFactory
{
    public UserBO getUserBO(UserPO userPO);

    public UserBORepository getUserBORepository();
}