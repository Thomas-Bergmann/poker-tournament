package de.hatoka.common.capi.resource;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.util.Locale;

import org.junit.Test;

import de.hatoka.common.capi.business.CountryHelper;

public class ResourceLocalizerTest
{
    private static final String RESOURCE_PREFIX = "de/hatoka/common/capi/resource/";

    private static ResourceLocalizer UNDER_TEST = new ResourceLocalizer(new LocalizationBundle(RESOURCE_PREFIX
                    + "resourceLocalizer", Locale.US, CountryHelper.UTC));

    @Test
    public void testInjectText()
    {
        assertEquals("resolved text", "A single text parameter is injected 'injectedText'.", UNDER_TEST.getText("inject.single", "Login", "injectedText"));
    }

    @Test
    public void testSimpleText()
    {
        assertEquals("resolved text", "Login Form", UNDER_TEST.getText("simple.text", "Login"));
        assertEquals("fallback text", "Login", UNDER_TEST.getText("simple.text.fallback", "Login"));
        assertNull("no fallback text", UNDER_TEST.getText("simple.text.nofallback", null));
    }

}
