package de.hatoka.offlinepoker.internal.app.frame;

import java.io.IOException;
import java.net.URI;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import org.custommonkey.xmlunit.XMLAssert;
import org.custommonkey.xmlunit.XMLUnit;
import org.junit.BeforeClass;
import org.junit.Test;

import de.hatoka.common.capi.app.xslt.Lib;
import de.hatoka.common.capi.app.xslt.XSLTRenderer;
import de.hatoka.common.capi.business.CountryHelper;
import de.hatoka.common.capi.resource.LocalizationBundle;
import de.hatoka.common.capi.resource.ResourceLoader;
import de.hatoka.common.capi.resource.ResourceLocalizer;
import de.hatoka.tournament.internal.app.models.FrameModel;

public class FrameRendererImplTest
{
    private static final String RESOURCE_PREFIX = "de/hatoka/offlinepoker/internal/templates/app/";
    private static final ResourceLoader RESOURCE_LOADER = new ResourceLoader();
    private static final XSLTRenderer RENDERER = new XSLTRenderer();

    private static Map<String, Object> getParameter()
    {
        Map<String, Object> result = new HashMap<>();
        result.put(Lib.XSLT_LOCALIZER, new ResourceLocalizer(
                        new LocalizationBundle(RESOURCE_PREFIX + "frame", Locale.US, CountryHelper.TZ_BERLIN)));
        return result;
    }


    private static String getResource(String resource) throws IOException
    {
        return RESOURCE_LOADER.getResourceAsString(RESOURCE_PREFIX + resource);
    }

    @BeforeClass
    public static void initClass()
    {
        XMLUnit.setIgnoreWhitespace(true);
    }

    @Test
    public void testLoggedInUser() throws Exception
    {
        FrameModel model = new FrameModel();
        model.setLoggedIn(true);
        model.setUriLogin(URI.create("http://localhost/login/login"));
        model.setUriLogout(URI.create("http://localhost/login/logout"));
        org.junit.Assert.assertEquals("loggedIn", getResource("frame_logged_in.result.xml"), renderContent(model));
        XMLAssert.assertXMLEqual("loggedIn", getResource("frame_logged_in.result.xml"), renderContent(model));
    }
    @Test
    public void testLoggedOutUser() throws Exception
    {
        FrameModel model = new FrameModel();
        model.setLoggedIn(false);
        model.setUriLogin(URI.create("http://localhost/login/login"));
        model.setUriLogout(URI.create("http://localhost/login/logout"));
        org.junit.Assert.assertEquals("loggedIn", getResource("frame_logged_out.result.xml"), renderContent(model));
        XMLAssert.assertXMLEqual("loggedOut", getResource("frame_logged_out.result.xml"), renderContent(model));
    }

    private static String renderContent(FrameModel model) throws IOException
    {
        return RENDERER.render(model, RESOURCE_PREFIX + "frame.xslt", getParameter());
    }
}
