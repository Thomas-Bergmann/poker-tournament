package de.hatoka.user.capi.business;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityTransaction;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import com.google.inject.Injector;

import de.hatoka.common.capi.exceptions.DuplicateObjectException;
import de.hatoka.test.DerbyEntityManagerRule;

public class UserBORepositoryTest
{
    private static final String USER_LOGIN_TEST = "test_user";
    private static UserBORepository UNDER_TEST;
    private List<EntityTransaction> transactions = new ArrayList<>();

    @Rule
    public DerbyEntityManagerRule rule = new DerbyEntityManagerRule("UserTestPU");

    @Before
    public void createTestObject()
    {
        Injector injector = TestBusinessInjectorProvider.get(rule.getModule());
        UserBusinessFactory factory = injector.getInstance(UserBusinessFactory.class);
        UNDER_TEST = factory.getUserBORepository();
    }

    private EntityTransaction createTransaction()
    {
        EntityTransaction result = rule.get().getTransaction();
        transactions.add(result);
        return result;
    }

    @After
    public void rollbackTransactions()
    {
        transactions.stream().filter(EntityTransaction::isActive).forEach(EntityTransaction::rollback);
    }

    @Test
    public void testCreateUser()
    {
        EntityTransaction transaction = createTransaction();
        transaction.begin();
        UserBO notFoundUser = UNDER_TEST.getUser(USER_LOGIN_TEST);
        assertNull("user not found", notFoundUser);
        UserBO user = UNDER_TEST.createUser(USER_LOGIN_TEST);
        assertNotNull("user not created", user);
        UserBO storedUser = UNDER_TEST.getUser(USER_LOGIN_TEST);
        assertNotNull("user not found", storedUser);
        assertEquals("user not equal", user, storedUser);
        transaction.rollback();
    }

    @Test(expected = DuplicateObjectException.class)
    public void testExistingUser()
    {
        EntityTransaction transaction = createTransaction();
        transaction.begin();
        UNDER_TEST.createUser(USER_LOGIN_TEST);
        UNDER_TEST.createUser(USER_LOGIN_TEST);
        transaction.rollback();
    }

}
