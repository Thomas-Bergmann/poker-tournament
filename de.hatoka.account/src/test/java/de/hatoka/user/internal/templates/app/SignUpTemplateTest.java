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
import de.hatoka.user.internal.app.forms.SignUpForm;
import de.hatoka.user.internal.app.models.LoginPageModel;

public class SignUpTemplateTest
{
    private static final String RESOURCE_PREFIX = "de/hatoka/user/internal/templates/app/";
    private static final String XSLT_STYLESHEET = RESOURCE_PREFIX + "login.xslt";
    private static final String XSLT_LOC = RESOURCE_PREFIX + "login";
    private static final XSLTRenderer RENDERER = new XSLTRenderer();
    private static final ResourceLoader RESOURCE_LOADER = new ResourceLoader();

    private String getResource(String resource) throws IOException
    {
        return RESOURCE_LOADER.getResourceAsString(RESOURCE_PREFIX + resource);
    }

    private String renderContent(LoginPageModel objects) throws IOException
    {
        return RENDERER.render(objects, XSLT_STYLESHEET, RENDERER.getParameter(XSLT_LOC, Locale.US, CountryHelper.UTC));
    }

    @Test
    public void testEmailExists() throws IOException, SAXException
    {
        SignUpForm form = new SignUpForm();
        form.setEmail("test@test.test");
        form.setEmailExists(true);
        LoginPageModel objects = new LoginPageModel();
        objects.setSignUpForm(form);
        XMLAssert.assertXMLEqual("signup_email_exists", getResource("signup_email_exists.result.xml"), renderContent(objects));
    }

    @BeforeClass
    public static void initClass()
    {
        XMLUnit.setIgnoreWhitespace(true);
    }

    @Test
    public void testSignUpShow() throws IOException, SAXException
    {
        SignUpForm form = new SignUpForm();
        LoginPageModel objects = new LoginPageModel();
        objects.setSignUpForm(form);
        XMLAssert.assertXMLEqual("signup_show", getResource("signup_show.result.xml"), renderContent(objects));
    }

    @Test
    public void testSuccess() throws IOException, SAXException
    {
        SignUpForm form = new SignUpForm();
        form.setEmail("test@test.test");
        form.setSuccessfullyRegistered(true);
        LoginPageModel objects = new LoginPageModel();
        objects.setSignUpForm(form);
        XMLAssert.assertXMLEqual("signup_success", getResource("signup_success.result.xml"), renderContent(objects));
    }

    @Test
    public void testXSS() throws IOException, SAXException
    {
        SignUpForm form = new SignUpForm();
        form.setEmail("<script type=\"text/javascript\">alert(\"XSS\");</script>");
        form.setSuccessfullyRegistered(true);
        LoginPageModel objects = new LoginPageModel();
        objects.setSignUpForm(form);
        XMLAssert.assertXMLEqual("signup_success", getResource("signup_success_xss.result.xml"), renderContent(objects));
    }
}
