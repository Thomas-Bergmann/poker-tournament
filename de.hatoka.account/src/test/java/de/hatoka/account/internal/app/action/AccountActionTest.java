package de.hatoka.account.internal.app.action;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;
import javax.persistence.EntityTransaction;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import com.google.inject.Guice;
import com.google.inject.Injector;

import de.hatoka.account.capi.business.AccountBusinessFactory;
import de.hatoka.account.capi.dao.UserDao;
import de.hatoka.account.capi.entities.UserPO;
import de.hatoka.account.internal.app.actions.AccountAction;
import de.hatoka.account.internal.dao.DerbyEntityManagerRule;
import de.hatoka.account.internal.modules.AccountBusinessModule;
import de.hatoka.account.internal.modules.AccountConfigurationModule;
import de.hatoka.account.internal.modules.AccountDaoJpaModule;
import de.hatoka.address.internal.modules.AddressBusinessModule;
import de.hatoka.address.internal.modules.AddressDaoModule;
import de.hatoka.common.capi.dao.TransactionProvider;
import de.hatoka.common.capi.modules.CommonDaoModule;
import de.hatoka.mail.internal.modules.MailDaoJpaModule;
import de.hatoka.mail.internal.modules.MailServiceConfigurationModule;
import de.hatoka.mail.internal.modules.MailServiceModule;

public class AccountActionTest
{
    private static final String ROOT_EMAIL = "TestUser";

    @Inject
    private AccountBusinessFactory factory;

    @Inject
    private TransactionProvider transactionProvider;

    @Inject
    private UserDao userDao;

    @Rule
    public DerbyEntityManagerRule rule = new DerbyEntityManagerRule();

    private AccountAction UNDER_TEST;
    private UserPO userPO;

    @Before
    public void createTestObject()
    {
        Injector injector = Guice.createInjector(new AccountBusinessModule(), new AddressBusinessModule(),new CommonDaoModule(), new AccountDaoJpaModule(), new AddressDaoModule(),
                        new MailDaoJpaModule(), new MailServiceModule(), new MailServiceConfigurationModule(), new AccountConfigurationModule(), rule.getModule());
        injector.injectMembers(this);
        EntityTransaction transaction = transactionProvider.get();
        transaction.begin();
        userPO = userDao.createAndInsert(ROOT_EMAIL);
        transaction.commit();
        UNDER_TEST = new AccountAction(factory.getUserBO(userPO));
    }

    @After
    public void removeUser()
    {
        EntityTransaction transaction = transactionProvider.get();
        transaction.begin();
        userDao.remove(userPO.getId());
        transaction.commit();
    }

    @Test
    public void testDeleteRelation()
    {
        List<String> accountIDs = new ArrayList<>();
        // create 1
        EntityTransaction transaction =  transactionProvider.get();
        transaction.begin();
        accountIDs.add(UNDER_TEST.createAccountBO("Test1").getID());
        accountIDs.add(UNDER_TEST.createAccountBO("Test2").getID());
        transaction.commit();
        UNDER_TEST.getListModel("12345");
        transaction.begin();
        UNDER_TEST.deleteAccounts(accountIDs, "12345");
        transaction.commit();
        UNDER_TEST.getListModel("12345");
    }

}
