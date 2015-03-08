package de.hatoka.common.capi.app.xslt;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;
import java.net.URL;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Map.Entry;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.util.JAXBSource;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;

import de.hatoka.common.capi.resource.LocalizationBundle;
import de.hatoka.common.capi.resource.ResourceLocalizer;

public class XSLTRenderer
{
    private final TransformerFactory transformerFactory = TransformerFactory.newInstance();

    public XSLTRenderer()
    {
        transformerFactory.setURIResolver(new ClassPathURIResolver());
    }

    public Map<String, Object> getParameter(String localizationResource, Locale locale)
    {
        Map<String, Object> result = new HashMap<>();
        result.put(Lib.XSLT_LOCALIZER, new ResourceLocalizer(new LocalizationBundle(localizationResource,
                        locale)));
        return result;
    }


    private StreamSource getStreamSource(String resource) throws FileNotFoundException
    {
        URL url = getClass().getClassLoader().getResource(resource);
        if (url == null)
        {
            throw new FileNotFoundException(resource);
        }
        return new StreamSource(url.toString());
    }

    /**
     * Render a jaxbElement with given xslt resource and
     * @param jaxbElement
     * @param stylesheet
     * @param parameters
     * @return
     * @throws IOException
     */
    public String render(Object jaxbModel, String stylesheet, Map<String, Object> parameters) throws IOException
    {
        StringWriter writer = new StringWriter();
        render(writer, jaxbModel, stylesheet, parameters);
        return writer.toString();
    }

    /**
     * Render a jaxbElement with given xslt resource and
     * @param writer write result to this writer
     * @param jaxbModel date to render
     * @param stylesheet xslt stylesheet resource
     * @param parameters parameter for style sheet, like localizer, uriInfo (depends on stylesheet)
     * @throws IOException
     */
    public void render(Writer writer, Object jaxbModel, String stylesheet, Map<String, Object> parameters) throws IOException
    {
        try
        {
            // Create Transformer
            Transformer transformer = transformerFactory.newTransformer(getStreamSource(stylesheet));
            for(Entry<String, Object> entry : parameters.entrySet())
            {
                transformer.setParameter(entry.getKey(), entry.getValue());
            }
            // Source
            JAXBContext jc = JAXBContext.newInstance(jaxbModel.getClass());

            // Transform
            transformer.transform(new JAXBSource(jc, jaxbModel), new StreamResult(writer));
        }
        catch(JAXBException | TransformerException e)
        {
            throw new IOException("Can't process stylesheet: " + stylesheet, e);
        }
    }
}
