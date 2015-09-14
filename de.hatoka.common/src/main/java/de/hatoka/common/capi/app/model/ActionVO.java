package de.hatoka.common.capi.app.model;

import java.net.URI;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

@XmlRootElement
public class ActionVO
{
    @XmlAttribute
    private String name;

    @XmlAttribute
    private String glyphicon;

    @XmlAttribute
    private URI uri;

    public ActionVO()
    {
    }

    public ActionVO(String name, URI uri, String glyphicon)
    {
        this.name = name;
        this.uri = uri;
        this.glyphicon = glyphicon;
    }

    @XmlTransient
    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    @XmlTransient
    public URI getUri()
    {
        return uri;
    }

    public void setUri(URI uri)
    {
        this.uri = uri;
    }

    @XmlTransient
    public String getGlyphicon()
    {
        return glyphicon;
    }

    public void setGlyphicon(String glyphicon)
    {
        this.glyphicon = glyphicon;
    }
}
