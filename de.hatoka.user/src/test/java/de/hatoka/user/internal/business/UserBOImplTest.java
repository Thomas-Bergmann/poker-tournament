package de.hatoka.user.internal.business;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Locale;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import de.hatoka.common.capi.locale.CountryHelper;
import de.hatoka.user.capi.business.UserBO;
import de.hatoka.user.capi.business.UserBORepository;
import de.hatoka.user.capi.business.UserRef;
import tests.de.hatoka.user.UserTestConfiguration;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = { UserTestConfiguration.class })
public class UserBOImplTest
{
    private static final CountryHelper COUNTRY_HELPER = new CountryHelper();
    private static final UserRef USER_LOGIN_TEST = UserRef.valueOfLocal("test_user");

    @Autowired
    private UserBORepository userRepo;

    @AfterEach @BeforeEach
    public void destroyCreatedObjects()
    {
        if (userRepo != null)
        {
            userRepo.clear();
        }
    }

    @Test
    public void testCommonData()
    {
        UserBO underTest = userRepo.createUser(USER_LOGIN_TEST);
        assertNotNull(underTest, "user exists");
        underTest.setLocale(Locale.GERMANY);
        underTest.setTimeZone(COUNTRY_HELPER.getTimeZone("Europe/Berlin"));
        assertEquals("DE", underTest.getLocale().getCountry());
        assertEquals("de", underTest.getLocale().getLanguage());

        assertTrue(underTest.getTimeZone().useDaylightTime(), "Germany is using DLST");
        underTest.remove();
    }
}
