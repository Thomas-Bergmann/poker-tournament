package de.hatoka.account.capi.business;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import com.google.inject.Injector;

import de.hatoka.test.DerbyEntityManagerRule;

public class AccountBORepositoryTest
{
    private static final String ROOT_ACCOUNT_LOGIN = "login";

    private UserBO userBO;
    private AccountBORepository UNDER_TEST;

    @Rule
    public DerbyEntityManagerRule rule = new DerbyEntityManagerRule("AccountTestPU");

    @After
    public void cleanUp()
    {
        // initialization failed
        if (UNDER_TEST == null)
        {
            return;
        }
        for(AccountBO account  : UNDER_TEST.getAccountBOs())
        {
            account.remove();
        }
        if(userBO != null)
        {
            userBO.remove();
        }
    }

    @Before
    public void create()
    {
        Injector injector = TestBusinessInjectorProvider.get(rule.getModule());
        AccountBusinessFactory factory = injector.getInstance(AccountBusinessFactory.class);
        userBO = factory.getUserBORepository().createUserBO(ROOT_ACCOUNT_LOGIN);
        UNDER_TEST = userBO.getAccountBORepository();
    }

    @Test
    public void testCreateAccount()
    {
        AccountBO account = UNDER_TEST.createAccountBO();
        assertNotNull("account not created", account);

        AccountBO accountByID = UNDER_TEST.getAccountBOById(account.getID());
        assertNotNull("Account not found via id", accountByID);
        assertEquals("Account not equal", account, accountByID);
    }
}
