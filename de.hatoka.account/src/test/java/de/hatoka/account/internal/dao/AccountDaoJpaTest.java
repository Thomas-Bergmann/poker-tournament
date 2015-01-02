package de.hatoka.account.internal.dao;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import javax.inject.Inject;
import javax.persistence.EntityTransaction;

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
import de.hatoka.common.capi.dao.TransactionProvider;
import de.hatoka.common.capi.modules.CommonDaoModule;

public class AccountDaoJpaTest
{
    private static final String ROOT_EMAIL = "root";

    @Rule
    public DerbyEntityManagerRule rule = new DerbyEntityManagerRule();
    @Inject
    private UserDao userDao;

    @Inject
    private AccountDao accountDao;
    @Inject
    private TransactionProvider transactionProvider;

    @Before
    public void createTestObject()
    {
        Injector injector = Guice.createInjector(new CommonDaoModule(), new AccountDaoJpaModule(), rule.getModule());
        injector.injectMembers(this);
    }

    @Test
    public void testCRUD()
    {
        EntityTransaction transaction = transactionProvider.get();
        transaction.begin();
        UserPO userPO = userDao.createAndInsert(ROOT_EMAIL);
        AccountPO accountPO = accountDao.createAndInsert(userPO);
        transaction.commit();

        AccountPO afterInsertAccountPO = accountDao.getById(accountPO.getId());
        assertNotNull("insert or get doesn't work", afterInsertAccountPO);

        transaction.begin();
        accountDao.remove(accountPO);
        transaction.commit();

        AccountPO afterDeleteAccountPO = accountDao.getById(accountPO.getId());
        assertNull("delete doesn't work, account still exist", afterDeleteAccountPO);
    }
}
