package de.hatoka.user.internal.business;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.Locale;

import org.junit.Test;

import de.hatoka.common.capi.business.CountryHelper;
import de.hatoka.user.capi.business.UserBO;
import de.hatoka.user.capi.entities.UserPO;
import de.hatoka.user.internal.business.UserBOImpl;

public class UserBOImplTest
{
    private static final CountryHelper COUNTRY_HELPER = new CountryHelper();

    @Test
    public void testCommonData()
    {
        UserPO userPO = new UserPO();
        UserBO underTest = new UserBOImpl(userPO);
        assertNotNull("user exists", underTest);
        underTest.setLocale(Locale.GERMANY);
        underTest.setTimeZone(COUNTRY_HELPER.getTimeZone("Europe/Berlin"));
        assertEquals("DE", underTest.getLocale().getCountry());
        assertEquals("de", underTest.getLocale().getLanguage());

        assertTrue("Germany is using DLST", underTest.getTimeZone().useDaylightTime());
    }
}
