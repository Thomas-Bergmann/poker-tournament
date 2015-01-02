package de.hatoka.account.internal.templates.app;

import static org.junit.Assert.assertEquals;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;
import java.util.Locale;

import org.apache.commons.io.IOUtils;
import org.junit.Test;

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
    public void testEmailExists() throws IOException
    {
        SignUpForm form = new SignUpForm();
        form.setEmail("test@test.test");
        form.setEmailExists(true);
        StringWriter writer = new StringWriter();
        LoginPageModel objects = new LoginPageModel();
        objects.setSignUpForm(form);
        getConverter(Locale.US).process(objects, XSLT_STYLESHEET, writer);
        assertEquals("signup_email_exists", trim(getResource("signup_email_exists.result.xml")), trim(writer));
    }

    @Test
    public void testSignUpShow() throws IOException
    {
        SignUpForm form = new SignUpForm();
        StringWriter writer = new StringWriter();
        LoginPageModel objects = new LoginPageModel();
        objects.setSignUpForm(form);
        getConverter(Locale.US).process(objects, XSLT_STYLESHEET, writer);
        assertEquals("signup_show", trim(getResource("signup_show.result.xml")), trim(writer));
    }

    @Test
    public void testSuccess() throws IOException
    {
        SignUpForm form = new SignUpForm();
        form.setEmail("test@test.test");
        form.setSuccessfullyRegistered(true);
        StringWriter writer = new StringWriter();
        LoginPageModel objects = new LoginPageModel();
        objects.setSignUpForm(form);
        getConverter(Locale.US).process(objects, XSLT_STYLESHEET, writer);
        assertEquals("signup_success", trim(getResource("signup_success.result.xml")), trim(writer));
    }

    @Test
    public void testXSS() throws IOException
    {
        SignUpForm form = new SignUpForm();
        form.setEmail("<script type=\"text/javascript\">alert(\"XSS\");</script>");
        form.setSuccessfullyRegistered(true);
        StringWriter writer = new StringWriter();
        LoginPageModel objects = new LoginPageModel();
        objects.setSignUpForm(form);
        getConverter(Locale.US).process(objects, XSLT_STYLESHEET, writer);
        assertEquals("signup_success", trim(getResource("signup_success_xss.result.xml")), trim(writer));
    }


    private String trim(String text)
    {
        return text.replace("\t", "    ").replace("\r\n","\n");
    }

    private String trim(StringWriter writer)
    {
        return trim(writer.toString());
    }
}
