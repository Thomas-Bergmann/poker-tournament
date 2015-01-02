package de.hatoka.common.capi.app.xslt;

import static org.junit.Assert.assertEquals;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;

import org.apache.commons.io.IOUtils;
import org.junit.Test;

public class ProcessorTest
{
    private static final String RESOURCE_PREFIX = "de/hatoka/common/capi/app/xslt/";
    private static final Processor CONVERTER = new Processor(RESOURCE_PREFIX);

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
    public void testModelToXML() throws IOException
    {
        LoginForm form = new LoginForm();
        form.setLoginFailed(true);
        form.setLogin("ThisIsTheLogin");

        /*
         * Execute the controller and process view template, writing the results
         * to the response writer.
         */
        StringWriter writer = new StringWriter();
        CONVERTER.process(form, writer);
        assertEquals("converter_model", trim(getResource("converter_model.xml")), trim(writer));
    }

    @Test
    public void testStyleSheetInsideOfXML() throws IOException
    {
        StringWriter writer = new StringWriter();
        CONVERTER.process("converter_embedded.xml", writer);
        assertEquals("converter_embbed", trim(getResource("converter_embedded.result")), trim(writer));
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
