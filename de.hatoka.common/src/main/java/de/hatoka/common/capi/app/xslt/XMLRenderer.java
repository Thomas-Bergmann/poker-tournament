package de.hatoka.common.capi.app.xslt;

import java.io.IOException;
import java.io.StringWriter;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;

public class XMLRenderer
{
    /**
     * Render a jaxbElement with given xslt resource and
     * @param jaxbModel
     * @return string representation of xml data object
     * @throws IOException
     */
    public String render(Object jaxbModel) throws IOException
    {
        StringWriter writer = new StringWriter();
        try
        {
            // Source
            JAXBContext jc = JAXBContext.newInstance(jaxbModel.getClass());
            // Transform
            Marshaller marshal = jc.createMarshaller();
            marshal.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
            marshal.marshal(jaxbModel, writer);
        }
        catch(JAXBException e)
        {
            throw new IOException("Can't produce xml.", e);
        }
        return writer.toString();
    }
}
