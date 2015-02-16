package de.hatoka.account.capi.business;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import javax.inject.Inject;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import de.hatoka.account.internal.dao.DerbyEntityManagerRule;
import de.hatoka.address.capi.business.AddressBO;

public class AccountBOTest
{
    private static final String USER_LOGIN_TEST = "test_user";
    private static AccountBO UNDER_TEST;

    @Rule
    public DerbyEntityManagerRule rule = new DerbyEntityManagerRule();

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
    public void testCreateAddress()
    {
        AddressBO addressBO = UNDER_TEST.getAddressBO();
        assertNull("address is set without need.", addressBO);
        addressBO = UNDER_TEST.createAddressBO();
        addressBO.setCity("GitHub");
        assertEquals("city of account address wasn't set", "GitHub", UNDER_TEST.getAddressBO().getCity());
    }

}
