package de.hatoka.user.capi.business;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import javax.persistence.EntityTransaction;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import com.google.inject.Injector;

import de.hatoka.test.DerbyEntityManagerRule;

public class UserBORepositoryTest
{
    private static final String USER_LOGIN_TEST = "test_user";
    private static UserBORepository UNDER_TEST;

    @Rule
    public DerbyEntityManagerRule rule = new DerbyEntityManagerRule("UserTestPU");

    @Before
    public void createTestObject()
    {
        Injector injector = TestBusinessInjectorProvider.get(rule.getModule());
        UserBusinessFactory factory = injector.getInstance(UserBusinessFactory.class);
        UNDER_TEST = factory.getUserBORepository();
    }

    @Test
    public void testCreateUser()
    {
        EntityTransaction transaction = rule.get().getTransaction();
        transaction.begin();
        UserBO user = UNDER_TEST.createUserBO(USER_LOGIN_TEST);
        assertNotNull("user not created", user);
        UserBO storedUser = UNDER_TEST.getUserBOByLogin(USER_LOGIN_TEST);
        assertNotNull("user not found", storedUser);
        assertEquals("user not equal", user, storedUser);
        transaction.rollback();
    }
}
