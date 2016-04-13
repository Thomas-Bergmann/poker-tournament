package de.hatoka.account.capi.business;

import static org.junit.Assert.assertNotNull;

import javax.inject.Inject;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import de.hatoka.test.DerbyEntityManagerRule;

public class AccountBOTest
{
    private static final String USER_LOGIN_TEST = "test_user";
    private static AccountBO UNDER_TEST;

    @Rule
    public DerbyEntityManagerRule rule = new DerbyEntityManagerRule("AccountTestPU");

    @Inject
    AccountBusinessFactory factory;
    private UserBO userBO;

    @Before
    public void createTestObject()
    {
        TestBusinessInjectorProvider.get(rule.getModule()).injectMembers(this);
        userBO = factory.getUserBORepository().createUserBO(USER_LOGIN_TEST);
        UNDER_TEST = userBO.getAccountBORepository().createAccountBO();
    }

    @Test
    public void testCreation()
    {
        assertNotNull("account exists", UNDER_TEST);
    }

}
