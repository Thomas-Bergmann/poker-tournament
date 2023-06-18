package de.hatoka.user.internal.business;

import org.springframework.beans.factory.annotation.Lookup;
import org.springframework.stereotype.Component;

import de.hatoka.user.capi.business.UserBO;
import de.hatoka.user.internal.persistence.UserPO;

@Component
public class UserBOFactoryImpl implements UserBOFactory
{
    @Lookup
    @Override
    public UserBO get(UserPO groupPO)
    {
        // done by @Lookup
        return null;
    }
}
