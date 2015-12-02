package de.hatoka.address.capi.dao;

import static org.junit.Assert.assertEquals;

import javax.inject.Inject;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import com.google.inject.Guice;
import com.google.inject.Injector;

import de.hatoka.address.capi.doa.AddressDao;
import de.hatoka.address.capi.entities.AddressPO;
import de.hatoka.address.internal.modules.AddressDaoModule;
import de.hatoka.common.capi.modules.CommonDaoModule;

public class AddressDaoTest
{
    private static final AddressPO ELEMENT = new AddressPO();

    private static final String ELEMENT_ID = "1234";

    @Inject
    private AddressDao underTest;

    @Rule
    public DerbyEntityManagerRule rule = new DerbyEntityManagerRule();
    @Before
    public void inject()
    {
        Injector injector = Guice.createInjector(new CommonDaoModule(), new AddressDaoModule(), rule.getModule());
        injector.injectMembers(this);
    }

    @Test(expected = IllegalStateException.class)
    public void testNullKey()
    {
        ELEMENT.setId(null);
        underTest.insert(ELEMENT);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNullValue()
    {
        underTest.insert(null);
    }

    @Test
    public void testStorage()
    {
        ELEMENT.setId(ELEMENT_ID);
        underTest.insert(ELEMENT);
        assertEquals("element not stored", ELEMENT_ID, underTest.getId(ELEMENT));
    }
}
