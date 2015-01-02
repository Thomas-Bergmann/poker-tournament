package de.hatoka.account.capi.business;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import javax.persistence.EntityTransaction;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import com.google.inject.Injector;

import de.hatoka.account.internal.dao.DerbyEntityManagerRule;

public class UserBORepositoryTest
{
    private static final String USER_LOGIN_TEST = "test_user";
    private static UserBORepository UNDER_TEST;

    @Rule
    public DerbyEntityManagerRule rule = new DerbyEntityManagerRule();

    @Before
    public void createTestObject()
    {
        Injector injector = TestBusinessInjectorProvider.get(rule.getModule());
        AccountBusinessFactory factory = injector.getInstance(AccountBusinessFactory.class);
        UNDER_TEST = factory.getUserBORepository();
    }

    @Test
    public void testCreateUser()
    {
        EntityTransaction transaction = rule.get().getTransaction();
        transaction.begin();
        UserBO user = UNDER_TEST.createUserBO(USER_LOGIN_TEST);
        assertNotNull("user not created", user);
        assertEquals("no accounts created for user", 1, user.getAccountBORepository().getAccountBOs().size());
        assertEquals("user not assigned to account", user, user.getAccountBORepository().getAccountBOs().get(0).getOwner());
        UserBO storedUser = UNDER_TEST.getUserBOByLogin(USER_LOGIN_TEST);
        assertNotNull("user not found", storedUser);
        assertEquals("user not equal", user, storedUser);
        transaction.rollback();
    }
}
