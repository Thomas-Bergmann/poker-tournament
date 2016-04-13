package de.hatoka.user.internal.modules;

import com.google.inject.Binder;
import com.google.inject.Module;

import de.hatoka.user.capi.dao.UserDao;
import de.hatoka.user.internal.dao.UserDaoJpa;

public class UserDaoJpaModule implements Module
{
    @Override
    public void configure(Binder binder)
    {
        binder.bind(UserDao.class).to(UserDaoJpa.class).asEagerSingleton();
    }
}
