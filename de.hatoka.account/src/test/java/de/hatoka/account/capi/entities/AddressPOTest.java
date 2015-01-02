package de.hatoka.account.capi.entities;

import org.junit.Test;

import de.hatoka.address.capi.entities.AddressPO;

public class AddressPOTest
{

    @Test
    public void testAllowOnlyCountryCodes()
    {
        AddressPO underTest = new AddressPO();
        underTest.setCountryID("NonExistingCountry");
    }

}
