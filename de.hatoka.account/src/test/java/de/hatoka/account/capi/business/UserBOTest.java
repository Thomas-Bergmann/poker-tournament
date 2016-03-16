package de.hatoka.account.capi.business;

import static org.junit.Assert.assertEquals;

import javax.inject.Inject;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import de.hatoka.account.capi.entities.UserPO;
import de.hatoka.address.capi.business.AddressBO;
import de.hatoka.address.capi.business.AddressBORepository;
import de.hatoka.test.DerbyEntityManagerRule;

public class UserBOTest
{
    private static UserBO UNDER_TEST;

    @Mock
    private AccountBO accountBO;

    @Inject
    private AccountBusinessFactory factory;

    @Rule
    public DerbyEntityManagerRule rule = new DerbyEntityManagerRule("AccountTestPU");

    @Before
    public void createTestObject()
    {
        TestBusinessInjectorProvider.get(rule.getModule()).injectMembers(this);

        MockitoAnnotations.initMocks(this);
        Mockito.when(accountBO.getID()).thenReturn("1");

        UNDER_TEST = factory.getUserBO(new UserPO());
    }

    @Test
    public void testAddressHandling()
    {
        AddressBORepository addressRepository = UNDER_TEST.getAddressBORepository();
        AddressBO addressBO = addressRepository.createAddressBO();
        addressBO.setCity("GitHub");
        UNDER_TEST.setPrivateAddressBO(addressBO);
        UNDER_TEST.setBusinessAddressBO(addressBO);
        assertEquals("address was not set", "GitHub", UNDER_TEST.getBusinessAddressBO().getCity());
    }
}
