package de.hatoka.account.internal.dao;

import javax.inject.Inject;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import com.google.inject.Guice;
import com.google.inject.Injector;

import de.hatoka.account.capi.dao.UserDao;
import de.hatoka.account.capi.entities.UserPO;
import de.hatoka.account.internal.modules.AccountDaoJpaModule;
import de.hatoka.common.capi.modules.CommonDaoModule;
import de.hatoka.test.DerbyEntityManagerRule;

public class UserDaoJpaTest
{
    private static final String ROOT_EMAIL = "root";
    @Rule
    public DerbyEntityManagerRule rule = new DerbyEntityManagerRule("AccountTestPU");

    @Inject
    private UserDao userDao;

    @Before
    public void createTestObject()
    {
        Injector injector = Guice.createInjector(new CommonDaoModule(), new AccountDaoJpaModule(), rule.getModule());
        injector.injectMembers(this);
    }

    @Test
    public void testUserCreation()
    {
        UserPO userPO = userDao.createAndInsert(ROOT_EMAIL);
        userDao.remove(userPO);
    }

}
