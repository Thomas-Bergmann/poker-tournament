package de.hatoka.account.internal.business;

import de.hatoka.account.capi.business.UserBO;
import de.hatoka.account.capi.business.UserBORepository;
import de.hatoka.account.capi.business.UserBusinessFactory;
import de.hatoka.account.capi.dao.UserDao;
import de.hatoka.account.capi.entities.UserPO;
import de.hatoka.common.capi.exceptions.DuplicateObjectException;

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
    public UserBO createUserBO(String login)
    {
        UserPO userPO = userDao.getByLogin(login);
        if (userPO != null)
        {
            throw new DuplicateObjectException("User with login exists: " + login);
        }
        userPO = userDao.createAndInsert(login);
        return businessFactory.getUserBO(userPO);
    }

    @Override
    public UserBO getUserBOByID(String userID)
    {
        UserPO userPO = userDao.getById(userID);
        if (userPO == null)
        {
            return null;
        }
        return businessFactory.getUserBO(userPO);
    }

    @Override
    public UserBO getUserBOByLogin(String login)
    {
        UserPO userPO = userDao.getByLogin(login);
        if (userPO == null)
        {
            return null;
        }
        return businessFactory.getUserBO(userPO);
    }
}
