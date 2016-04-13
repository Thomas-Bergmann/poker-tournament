package de.hatoka.user.internal.business;

import javax.inject.Inject;

import com.google.inject.Injector;

import de.hatoka.common.capi.exceptions.MandatoryParameterException;
import de.hatoka.user.capi.business.UserBO;
import de.hatoka.user.capi.business.UserBORepository;
import de.hatoka.user.capi.business.UserBusinessFactory;
import de.hatoka.user.capi.dao.UserDao;
import de.hatoka.user.capi.entities.UserPO;

public class UserBusinessFactoryImpl implements UserBusinessFactory
{
    @Inject
    private UserDao userDao;
    @Inject
    private Injector injector;

    @Inject
    private UserBusinessFactoryImpl()
    {

    }

    @Override
    public UserBO getUserBO(UserPO userPO)
    {
        if (userPO == null)
        {
            throw new MandatoryParameterException("userPO");
        }

        UserBO accountUserBO = new UserBOImpl(userPO);
        injector.injectMembers(accountUserBO);
        return accountUserBO;
    }

    @Override
    public UserBORepository getUserBORepository()
    {
        return new UserBORepositoryImpl(userDao, this);
    }
}
