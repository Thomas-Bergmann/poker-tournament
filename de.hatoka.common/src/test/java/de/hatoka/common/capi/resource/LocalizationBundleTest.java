package de.hatoka.common.capi.resource;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.Locale;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

import org.junit.Test;

public class LocalizationBundleTest
{
    private static final LocalizationBundle UNDER_TEST_EN_US = new LocalizationBundle("de/hatoka/common/capi/resource/localization", Locale.US);

    @Test(expected=MissingResourceException.class)
    public void testCrazyPackage()
    {
        ResourceBundle bundleSlashVersion = ResourceBundle.getBundle("de/hatoka/common/capi/resource/1_2_3/localization", Locale.US);
        assertNotNull("bundleSlashVersion", bundleSlashVersion);
        ResourceBundle.getBundle("de/hatoka/common/capi/resource/1.2.3/localization", Locale.US);
    }

    @Test
    public void testFallbackToLanguage()
    {
        assertEquals("fallback lookup doesn't work", "EN String", UNDER_TEST_EN_US.getText("fallbackTestKey"));
    }

    @Test
    public void testLookup()
    {
        assertEquals("standard lookup doesn't work", "EN US String", UNDER_TEST_EN_US.getText("testKey"));
    }
    @Test
    public void testNonExistingKey()
    {
        assertEquals("standard lookup doesn't work", "{nonExistingKey}", UNDER_TEST_EN_US.getText("nonExistingKey"));
    }
}
