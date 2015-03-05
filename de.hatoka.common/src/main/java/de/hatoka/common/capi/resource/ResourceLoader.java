package de.hatoka.common.capi.resource;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;

import org.apache.commons.io.IOUtils;

public class ResourceLoader
{
    public String getResourceAsString(String resource) throws IOException
    {
        StringWriter writer = new StringWriter();
        IOUtils.copy(getResourceAsStream(resource), writer, "UTF-8");
        return writer.toString();
    }

    public InputStream getResourceAsStream(String resource) throws IOException
    {
        InputStream resourceAsStream = getClass().getClassLoader().getResourceAsStream(resource);
        if (resourceAsStream == null)
        {
            throw new FileNotFoundException("Can't find resource: " + resource);
        }
        return resourceAsStream;
    }
}
