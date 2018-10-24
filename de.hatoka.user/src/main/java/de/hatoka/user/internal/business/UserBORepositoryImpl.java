package de.hatoka.user.internal.business;

import de.hatoka.common.capi.exceptions.DuplicateObjectException;
import de.hatoka.user.capi.business.UserBO;
import de.hatoka.user.capi.business.UserBORepository;
import de.hatoka.user.capi.business.UserBusinessFactory;
import de.hatoka.user.capi.dao.UserDao;
import de.hatoka.user.capi.entities.UserPO;

public class UserBORepositoryImpl implements UserBORepository
{
    private final UserDao userDao;
    private final UserBusinessFactory businessFactory;

    public UserBORepositoryImpl(UserDao userDao, UserBusinessFactory businessFactory)
    {
        this.userDao = userDao;
        this.businessFactory = businessFactory;
    }

    @Override
    public UserBO createUser(String externalRef)
    {
        UserPO userPO = userDao.getByExternalRef(externalRef);
        if (userPO != null)
        {
            throw new DuplicateObjectException("User exists: " + externalRef);
        }
        userPO = userDao.createAndInsert(externalRef);
        return businessFactory.getUserBO(userPO);
    }

    @Override
    public UserBO getUser(String externalRef)
    {
        UserPO userPO = userDao.getByExternalRef(externalRef);
        if (userPO == null)
        {
            return null;
        }
        return businessFactory.getUserBO(userPO);
    }
}
