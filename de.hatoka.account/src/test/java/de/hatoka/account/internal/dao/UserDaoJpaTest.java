package de.hatoka.account.internal.dao;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import javax.inject.Inject;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import com.google.inject.Guice;
import com.google.inject.Injector;

import de.hatoka.account.capi.dao.AccountDao;
import de.hatoka.account.capi.dao.UserDao;
import de.hatoka.account.capi.entities.AccountPO;
import de.hatoka.account.capi.entities.UserPO;
import de.hatoka.account.internal.modules.AccountDaoJpaModule;
import de.hatoka.common.capi.modules.CommonDaoModule;

public class UserDaoJpaTest
{
    private static final String ROOT_EMAIL = "root";
    @Rule
    public DerbyEntityManagerRule rule = new DerbyEntityManagerRule();

    @Inject
    private AccountDao accountDao;

    @Inject
    private UserDao userDao;

    @Before
    public void createTestObject()
    {
        Injector injector = Guice.createInjector(new CommonDaoModule(), new AccountDaoJpaModule(), rule.getModule());
        injector.injectMembers(this);
    }

    @Test
    public void testAccountUserAssignmentSideAccount()
    {
        UserPO userPO = userDao.createAndInsert(ROOT_EMAIL);
        AccountPO accountPO = accountDao.createAndInsert(userPO);
        assertTrue("user should be assigned to account", userPO.getAccountPOs().contains(accountPO));
        assertEquals("account should be assigned to user", userPO, accountPO.getOwner());

        accountDao.remove(accountPO);
        assertFalse("user should not be assigned at account", userPO.getAccountPOs().contains(accountPO));
        userDao.remove(userPO);
    }

}
