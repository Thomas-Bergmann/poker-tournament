package de.hatoka.common.capi.app.xslt;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.Writer;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import javax.ws.rs.core.UriInfo;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.util.JAXBSource;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;

public class Processor
{
    private final String resourceLocation;
    private final TransformerFactory transformerFactory = TransformerFactory.newInstance();
    private final Map<String, Object> globalParameters = new HashMap<>();

    /**
     * Creates a xslt processor. Use {@link #process(Object, Writer)} to render
     * your model.
     *
     * @param resourceLocation
     *            location of stylesheets, templates and other xslt resources
     */
    public Processor(String resourceLocation)
    {
        this(resourceLocation, new Localizer()
        {
            @Override
            public String formatDate(String dateString)
            {
                return dateString;
            }

            @Override
            public String getText(String key, String defaulText, Object... objects)
            {
                return defaulText;
            }
        });
    }

    /**
     * Creates a xslt processor. Use {@link #process(Object, Writer)} to render
     * your model.
     *
     * @param resourceLocation
     *            location of stylesheets, templates and other xslt resources
     * @param localizer
     *            localizes text snippets (used via xsl:param)
     */
    public Processor(String resourceLocation, Localizer localizer)
    {
        this.resourceLocation = resourceLocation;
        globalParameters.put("localizer", localizer);
        transformerFactory.setURIResolver(new ClassPathURIResolver());
    }

    private StreamSource getStreamSource(String resource) throws FileNotFoundException
    {
        URL url = getClass().getClassLoader().getResource(resourceLocation + resource);
        if (url == null)
        {
            throw new FileNotFoundException(resourceLocation + resource);
        }
        return new StreamSource(url.toString());
    }

    /**
     * Process a jaxbElement with the given style sheet.
     *
     * @param jaxbElement
     * @param stylesheet
     * @param writer
     * @throws IOException
     */
    public void process(Object jaxbElement, String stylesheet, Writer writer) throws IOException
    {
        Map<String, Object> parameters = new HashMap<>();
        parameters.putAll(globalParameters);
        process(jaxbElement, stylesheet, writer, parameters);
    }

    /**
     * Process a jaxbElement with the given style sheet.
     *
     * @param jaxbElement
     * @param stylesheet
     * @param writer
     * @param localParameters of transformer (can be used with &lt;xsl:param name="key" /&gt;)
     * @throws IOException
     */
    public void process(Object jaxbElement, String stylesheet, Writer writer, Map<String, Object> localParameters) throws IOException
    {
        Map<String, Object> parameters = new HashMap<String, Object>(globalParameters);
        parameters.putAll(localParameters);
        try
        {
            // Create Transformer
            Transformer transformer = transformerFactory.newTransformer(getStreamSource(stylesheet));
            for(Entry<String, Object> entry : parameters.entrySet())
            {
                transformer.setParameter(entry.getKey(), entry.getValue());
            }
            // Source
            JAXBContext jc = JAXBContext.newInstance(jaxbElement.getClass());

            // Transform
            transformer.transform(new JAXBSource(jc, jaxbElement), new StreamResult(writer));
        }
        catch(JAXBException | TransformerException e)
        {
            throw new IOException("Can't process stylesheet: " + stylesheet, e);
        }
    }

    /**
     * Process a jaxbElement with the given style sheet.
     *
     * @param jaxbElement
     * @param stylesheet
     * @param writer
     * @throws IOException
     */
    public void process(Object jaxbElement, String stylesheet, Writer writer, UriInfo uriInfo) throws IOException
    {
        Map<String, Object> parameters = new HashMap<>();
        parameters.put("uriInfo", uriInfo);
        process(jaxbElement, stylesheet, writer, parameters);
    }

    /**
     * Process a jaxbElement to plain serialized xml.
     *
     * @param jaxbElement
     * @param stylesheet
     * @param writer
     * @throws IOException
     */
    public void process(Object jaxbElement, Writer writer) throws IOException
    {
        try
        {
            // Source
            JAXBContext jc = JAXBContext.newInstance(jaxbElement.getClass());
            // Transform
            Marshaller marshal = jc.createMarshaller();
            marshal.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
            marshal.marshal(jaxbElement, writer);
        }
        catch(JAXBException e)
        {
            throw new IOException(e);
        }

    }

    /**
     * Process a xml resource, which contains the style sheet
     *
     * @param xmlResource
     * @param writer
     * @throws IOException
     */
    public void process(String xmlResource, String styleSheet, Writer writer) throws IOException
    {
        try
        {
            Transformer transformer = transformerFactory.newTransformer(getStreamSource(styleSheet));
            transformer.transform(getStreamSource(xmlResource), new StreamResult(writer));
        }
        catch(TransformerException e)
        {
            throw new IOException(e);
        }
    }

    /**
     * Process a xml resource, which contains the style sheet
     *
     * @param xmlResource
     * @param writer
     * @throws IOException
     */
    public void process(String xmlResource, Writer writer) throws IOException
    {
        try
        {
            // get style sheet from the XML source.
            Source stylesheet = transformerFactory.getAssociatedStylesheet(getStreamSource(xmlResource), null, null,
                            null);
            if (stylesheet == null)
            {
                throw new FileNotFoundException("Style sheet not found at:" + xmlResource);
            }
            // transform
            Transformer transformer = transformerFactory.newTransformer(stylesheet);
            if (transformer == null)
            {
                throw new IOException("Can't create transformer for stylesheet:" + stylesheet.getSystemId());
            }
            transformer.transform(getStreamSource(xmlResource), new StreamResult(writer));
        }
        catch(TransformerException e)
        {
            throw new IOException(e);
        }
    }
}
