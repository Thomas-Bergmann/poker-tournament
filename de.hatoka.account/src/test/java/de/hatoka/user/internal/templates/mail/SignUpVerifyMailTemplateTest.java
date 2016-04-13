package de.hatoka.user.internal.templates.mail;

import java.io.IOException;
import java.util.Locale;

import org.custommonkey.xmlunit.XMLAssert;
import org.custommonkey.xmlunit.XMLUnit;
import org.junit.BeforeClass;
import org.junit.Test;
import org.xml.sax.SAXException;

import de.hatoka.common.capi.app.xslt.XSLTRenderer;
import de.hatoka.common.capi.business.CountryHelper;
import de.hatoka.common.capi.resource.ResourceLoader;
import de.hatoka.user.internal.app.models.SignUpVerifyMailModel;

public class SignUpVerifyMailTemplateTest
{
    private static final String RESOURCE_PREFIX = "de/hatoka/user/internal/templates/mail/";
    private static final XSLTRenderer RENDERER = new XSLTRenderer();
    private static final ResourceLoader RESOURCE_LOADER = new ResourceLoader();

    private String getResource(String resource) throws IOException
    {
        return RESOURCE_LOADER.getResourceAsString(RESOURCE_PREFIX + resource);
    }

    private String renderContent(Object objects) throws IOException
    {
        return RENDERER.render(objects, RESOURCE_PREFIX + "signUpVerifyEmail.html.xslt", RENDERER.getParameter(RESOURCE_PREFIX + "signup", Locale.US, CountryHelper.UTC));
    }

    @BeforeClass
    public static void initClass()
    {
        XMLUnit.setIgnoreWhitespace(true);
    }

    @Test
    public void testMail() throws IOException, SAXException
    {
        SignUpVerifyMailModel model = new SignUpVerifyMailModel();
        model.setLink("http://test.test/acceptEMail/token");
        XMLAssert.assertXMLEqual("signUpVerifyEmail.html", getResource("signUpVerifyEmail.result.xml"), renderContent(model));
    }
}
