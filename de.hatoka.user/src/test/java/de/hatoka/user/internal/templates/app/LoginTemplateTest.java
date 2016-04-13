package de.hatoka.user.internal.templates.app;

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
import de.hatoka.user.internal.app.forms.LoginForm;

public class LoginTemplateTest
{
    private static final String ORIGIN = "http://testdomain.de/origin";
    private static final String RESOURCE_PREFIX = "de/hatoka/user/internal/templates/app/";
    private static final XSLTRenderer RENDERER = new XSLTRenderer();
    private static final ResourceLoader RESOURCE_LOADER = new ResourceLoader();

    private String getResource(String resource) throws IOException
    {
        return RESOURCE_LOADER.getResourceAsString(RESOURCE_PREFIX + resource);
    }

    private String renderContent(Object objects, Locale locale) throws IOException
    {
        return RENDERER.render(objects, RESOURCE_PREFIX + "login.xslt", RENDERER.getParameter(RESOURCE_PREFIX + "login", locale, CountryHelper.UTC));
    }

    private String renderContent(Object objects) throws IOException
    {
        return renderContent(objects, Locale.US);
    }

    @BeforeClass
    public static void initClass()
    {
        XMLUnit.setIgnoreWhitespace(true);
    }

    @Test
    public void testLoginFailed() throws IOException, SAXException
    {
        LoginForm form = new LoginForm();
        form.setOrigin(ORIGIN);
        form.setLoginFailed(true);
        XMLAssert.assertXMLEqual("login_failed", getResource("login_failed.result.xml"), renderContent(form));
    }

    @Test
    public void testLoginShow() throws IOException, SAXException
    {
        LoginForm form = new LoginForm();
        form.setOrigin(ORIGIN);
        XMLAssert.assertXMLEqual("login_show", getResource("login_show.result.xml"), renderContent(form));
    }

    @Test
    public void testLoginShowDE() throws IOException, SAXException
    {
        LoginForm form = new LoginForm();
        form.setOrigin(ORIGIN);
        XMLAssert.assertXMLEqual("login_show", getResource("login_show_de_DE.result.xml"), renderContent(form, Locale.GERMANY));
    }

    @Test
    public void testSignInSuccess() throws IOException, SAXException
    {
        LoginForm form = new LoginForm();
        form.setOrigin(ORIGIN);
        form.setEmail("login@test.test");
        form.setNowActive(true);
        XMLAssert.assertXMLEqual("login_signin_success", getResource("login_signin_success.result.xml"), renderContent(form));
    }

}
