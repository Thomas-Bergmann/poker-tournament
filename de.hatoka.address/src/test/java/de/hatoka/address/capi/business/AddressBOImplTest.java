package de.hatoka.address.capi.business;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import javax.inject.Inject;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import com.google.inject.Guice;
import com.google.inject.Injector;

import de.hatoka.address.capi.doa.AddressDao;
import de.hatoka.address.capi.entities.AddressPO;
import de.hatoka.address.internal.business.AddressBOImpl;
import de.hatoka.address.internal.modules.AddressDaoModule;
import de.hatoka.common.capi.exceptions.CountryNotExistException;
import de.hatoka.common.capi.modules.CommonDaoModule;
import de.hatoka.test.DerbyEntityManagerRule;

public class AddressBOImplTest
{
    private static final AddressPO ADDRESS_EMPTY = new AddressPO("(empty)");

    @Inject
    private AddressDao addressDao;

    private AddressBOImpl underTest;

    @Rule
    public DerbyEntityManagerRule rule = new DerbyEntityManagerRule();

    @Before
    public void init()
    {
        Injector injector = Guice.createInjector(new CommonDaoModule(), new AddressDaoModule(), rule.getModule());
        injector.injectMembers(this);
        underTest = new AddressBOImpl(ADDRESS_EMPTY, null, addressDao);
    }

    @Test
    public void testCountryCodeSet()
    {
        underTest.setCountryID("US");
        assertEquals("Country code wasn't set", "US", underTest.getCountryID());
        assertNull("AddressBO shouls not modify original address", ADDRESS_EMPTY.getCountryID());
    }

    @Test
    public void testModification()
    {
        underTest.setCountryID("DE");
        assertTrue("Address doesn't reflects, that it was changed.", underTest.isModified());
    }


    @Test(expected = CountryNotExistException.class)
    public void testValidateCountryCodeFailure()
    {
        underTest.setCountryID("NO_MAN_S_LAND");
    }
}
