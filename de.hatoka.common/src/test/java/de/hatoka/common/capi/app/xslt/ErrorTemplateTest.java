package de.hatoka.common.capi.app.xslt;

import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.io.StringWriter;
import java.util.Locale;

import org.junit.Test;

import de.hatoka.common.capi.business.CountryHelper;
import de.hatoka.common.internal.app.models.ErrorModel;

public class ErrorTemplateTest
{
    private static final String RESOURCE_PREFIX = "de/hatoka/common/capi/app/xslt/";
    private static final XSLTRenderer RENDERER = new XSLTRenderer();

    @Test
    public void testExceptionOutput() throws IOException
    {
        try{
            throw new RuntimeException("Test Exception");
        }
        catch(RuntimeException e)
        {
            StringWriter writer = new StringWriter();
            RENDERER.render(writer, new ErrorModel("1234", e), RESOURCE_PREFIX + "error.xslt", RENDERER.getParameter(RESOURCE_PREFIX + "error", Locale.US, CountryHelper.UTC));
            assertTrue("headline", trim(writer).contains("<h2>Error: 1234</h2>"));
            assertTrue("message", trim(writer).contains("<div class=\"message\">Test Exception</div>"));
        }
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
