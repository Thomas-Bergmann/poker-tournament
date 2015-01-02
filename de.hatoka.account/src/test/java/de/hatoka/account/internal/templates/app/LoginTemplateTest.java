package de.hatoka.account.internal.templates.app;

import static org.junit.Assert.assertEquals;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;
import java.util.Locale;

import org.apache.commons.io.IOUtils;
import org.junit.Test;

import de.hatoka.account.internal.app.forms.LoginForm;
import de.hatoka.common.capi.app.xslt.Processor;
import de.hatoka.common.capi.resource.LocalizationBundle;
import de.hatoka.common.capi.resource.ResourceLocalizer;

public class LoginTemplateTest
{
    private static final String XSLT_STYLESHEET = "login.xslt";
    private static final String ORIGIN = "http://testdomain.de/origin";
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
    public void testLoginFailed() throws IOException
    {
        LoginForm form = new LoginForm();
        form.setOrigin(ORIGIN);
        form.setLoginFailed(true);
        StringWriter writer = new StringWriter();
        getConverter(Locale.US).process(form, XSLT_STYLESHEET, writer);
        assertEquals("login_failed", trim(getResource("login_failed.result.xml")), trim(writer));
    }

    @Test
    public void testLoginShow() throws IOException
    {
        LoginForm form = new LoginForm();
        form.setOrigin(ORIGIN);
        StringWriter writer = new StringWriter();
        getConverter(Locale.US).process(form, XSLT_STYLESHEET, writer);
        assertEquals("login_show", trim(getResource("login_show.result.xml")), trim(writer));
    }

    @Test
    public void testLoginShowDE() throws IOException
    {
        LoginForm form = new LoginForm();
        form.setOrigin(ORIGIN);
        StringWriter writer = new StringWriter();
        getConverter(Locale.GERMANY).process(form, XSLT_STYLESHEET, writer);
        assertEquals("login_show", trim(getResource("login_show_de_DE.result.xml")), trim(writer));
    }

    @Test
    public void testSignInSuccess() throws IOException
    {
        LoginForm form = new LoginForm();
        form.setOrigin(ORIGIN);
        form.setLogin("login@test.test");
        form.setNowActive(true);
        StringWriter writer = new StringWriter();
        getConverter(Locale.US).process(form, XSLT_STYLESHEET, writer);
        // output of data getConverter(Locale.US).process(form, writer);
        assertEquals("login_signin_success", trim(getResource("login_signin_success.result.xml")), trim(writer));
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
