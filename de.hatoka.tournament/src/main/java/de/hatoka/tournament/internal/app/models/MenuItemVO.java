package de.hatoka.tournament.internal.app.models;

import java.net.URI;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlTransient;

public class MenuItemVO
{
    @XmlAttribute
    private String title;
    @XmlAttribute
    private String titleKey;
    @XmlAttribute
    private URI uri;
    @XmlAttribute
    private boolean isActive;
    @XmlAttribute
    private Integer count;
    @XmlAttribute
    private URI uriAdd;

    public MenuItemVO(String titleKey, URI uri, boolean isActive)
    {
        this.titleKey = titleKey;
        this.uri = uri;
        this.isActive = isActive;
    }

    public MenuItemVO(String titleKey, URI uriList, boolean isActive, int count, URI uriAdd)
    {
        this(titleKey, uriList, isActive);
        this.count = count;
        this.uriAdd = uriAdd;
    }

    @XmlTransient
    public Integer getCount()
    {
        return count;
    }

    @XmlTransient
    public URI getUriAdd()
    {
        return uriAdd;
    }

    public void setCount(Integer count)
    {
        this.count = count;
    }

    public void setUriAdd(URI uriAdd)
    {
        this.uriAdd = uriAdd;
    }

    public MenuItemVO()
    {
    }

    @XmlTransient
    public String getTitle()
    {
        return title;
    }

    @XmlTransient
    public URI getUri()
    {
        return uri;
    }

    @XmlTransient
    public boolean isActive()
    {
        return isActive;
    }

    public void setTitle(String title)
    {
        this.title = title;
    }

    public void setUri(URI uri)
    {
        this.uri = uri;
    }

    public void setActive(boolean isActive)
    {
        this.isActive = isActive;
    }

    @XmlTransient
    public String getTitleKey()
    {
        return titleKey;
    }

    public void setTitleKey(String titleKey)
    {
        this.titleKey = titleKey;
    }
}
