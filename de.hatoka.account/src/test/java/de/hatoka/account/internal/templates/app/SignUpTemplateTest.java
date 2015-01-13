package de.hatoka.account.internal.templates.app;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;
import java.util.Locale;

import org.apache.commons.io.IOUtils;
import org.custommonkey.xmlunit.XMLAssert;
import org.custommonkey.xmlunit.XMLUnit;
import org.junit.BeforeClass;
import org.junit.Test;
import org.xml.sax.SAXException;

import de.hatoka.account.internal.app.forms.SignUpForm;
import de.hatoka.account.internal.app.models.LoginPageModel;
import de.hatoka.common.capi.app.xslt.Processor;
import de.hatoka.common.capi.resource.LocalizationBundle;
import de.hatoka.common.capi.resource.ResourceLocalizer;

public class SignUpTemplateTest
{
    private static final String XSLT_STYLESHEET = "login.xslt";
    private static final String RESOURCE_PREFIX = "de/hatoka/account/internal/templates/app/";

    private Processor getConverter(Locale locale)
    {
        return new Processor(RESOURCE_PREFIX, new ResourceLocalizer(new LocalizationBundle(RESOURCE_PREFIX + "login", locale)));
    }

    private String getResource(String string) throws IOException
    {
        StringWriter writer = new StringWriter();
        InputStream resourceAsStream = getClass().getClassLoader().getResourceAsStream(RESOURCE_PREFIX + string);
        if (resourceAsStream == null)
        {
            throw new FileNotFoundException("Can't find resource: " + RESOURCE_PREFIX + string);
        }
        IOUtils.copy(resourceAsStream, writer, "UTF-8");
        return writer.toString();
    }

    @Test
    public void testEmailExists() throws IOException, SAXException
    {
        SignUpForm form = new SignUpForm();
        form.setEmail("test@test.test");
        form.setEmailExists(true);
        StringWriter writer = new StringWriter();
        LoginPageModel objects = new LoginPageModel();
        objects.setSignUpForm(form);
        getConverter(Locale.US).process(objects, XSLT_STYLESHEET, writer);
        XMLAssert.assertXMLEqual("signup_email_exists", getResource("signup_email_exists.result.xml"), writer.toString());
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
        StringWriter writer = new StringWriter();
        LoginPageModel objects = new LoginPageModel();
        objects.setSignUpForm(form);
        getConverter(Locale.US).process(objects, XSLT_STYLESHEET, writer);
        XMLAssert.assertXMLEqual("signup_show", getResource("signup_show.result.xml"), writer.toString());
    }

    @Test
    public void testSuccess() throws IOException, SAXException
    {
        SignUpForm form = new SignUpForm();
        form.setEmail("test@test.test");
        form.setSuccessfullyRegistered(true);
        StringWriter writer = new StringWriter();
        LoginPageModel objects = new LoginPageModel();
        objects.setSignUpForm(form);
        getConverter(Locale.US).process(objects, XSLT_STYLESHEET, writer);
        XMLAssert.assertXMLEqual("signup_success", getResource("signup_success.result.xml"), writer.toString());
    }

    @Test
    public void testXSS() throws IOException, SAXException
    {
        SignUpForm form = new SignUpForm();
        form.setEmail("<script type=\"text/javascript\">alert(\"XSS\");</script>");
        form.setSuccessfullyRegistered(true);
        StringWriter writer = new StringWriter();
        LoginPageModel objects = new LoginPageModel();
        objects.setSignUpForm(form);
        getConverter(Locale.US).process(objects, XSLT_STYLESHEET, writer);
        XMLAssert.assertXMLEqual("signup_success", getResource("signup_success_xss.result.xml"), writer.toString());
    }
}
