package de.hatoka.account.internal.templates.mail;

import static org.junit.Assert.assertEquals;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;
import java.util.Locale;

import org.apache.commons.io.IOUtils;
import org.junit.Test;

import de.hatoka.account.internal.app.models.SignUpVerifyMailModel;
import de.hatoka.common.capi.app.xslt.Processor;
import de.hatoka.common.capi.resource.LocalizationBundle;
import de.hatoka.common.capi.resource.ResourceLocalizer;

public class SignUpVerifyMailTemplateTest
{
    private static final String RESOURCE_PREFIX = "de/hatoka/account/internal/templates/mail/";

    private Processor getConverter(Locale locale)
    {
        return new Processor(RESOURCE_PREFIX, new ResourceLocalizer(new LocalizationBundle(RESOURCE_PREFIX + "signup", locale)));
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
    public void testMail() throws IOException
    {
        SignUpVerifyMailModel model = new SignUpVerifyMailModel();
        model.setLink("http://test.test/acceptEMail/token");
        StringWriter writer = new StringWriter();
        getConverter(Locale.US).process(model, "signUpVerifyEmail.html.xslt", writer);
        assertEquals("signUpVerifyEmail.html", trim(getResource("signUpVerifyEmail.html.result")), trim(writer));
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
