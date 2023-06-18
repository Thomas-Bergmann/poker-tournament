package de.hatoka.user.internal.business;

import de.hatoka.user.capi.business.UserBO;
import de.hatoka.user.internal.persistence.UserPO;

public interface UserBOFactory
{
    UserBO get(UserPO userPO);
}
