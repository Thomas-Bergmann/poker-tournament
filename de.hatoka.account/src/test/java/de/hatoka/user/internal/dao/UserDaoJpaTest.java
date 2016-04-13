package de.hatoka.user.internal.dao;

import javax.inject.Inject;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import com.google.inject.Guice;
import com.google.inject.Injector;

import de.hatoka.common.capi.modules.CommonDaoModule;
import de.hatoka.test.DerbyEntityManagerRule;
import de.hatoka.user.capi.dao.UserDao;
import de.hatoka.user.capi.entities.UserPO;
import de.hatoka.user.internal.modules.UserDaoJpaModule;

public class UserDaoJpaTest
{
    private static final String ROOT_EMAIL = "root";
    @Rule
    public DerbyEntityManagerRule rule = new DerbyEntityManagerRule("UserTestPU");

    @Inject
    private UserDao userDao;

    @Before
    public void createTestObject()
    {
        Injector injector = Guice.createInjector(new CommonDaoModule(), new UserDaoJpaModule(), rule.getModule());
        injector.injectMembers(this);
    }

    @Test
    public void testUserCreation()
    {
        UserPO userPO = userDao.createAndInsert(ROOT_EMAIL);
        userDao.remove(userPO);
    }

}
