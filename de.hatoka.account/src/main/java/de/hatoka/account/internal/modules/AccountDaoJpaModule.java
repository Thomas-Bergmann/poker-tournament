package de.hatoka.account.internal.modules;

import com.google.inject.Binder;
import com.google.inject.Module;

import de.hatoka.account.capi.dao.UserDao;
import de.hatoka.account.internal.dao.UserDaoJpa;

public class AccountDaoJpaModule implements Module
{
    @Override
    public void configure(Binder binder)
    {
        binder.bind(UserDao.class).to(UserDaoJpa.class).asEagerSingleton();
    }
}
