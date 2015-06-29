package de.hatoka.common.capi.app.model;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlTransient;


public class SelectOptionVO
{
    @XmlAttribute
    private String name;

    @XmlAttribute
    private boolean isSelected = false;

    public SelectOptionVO()
    {
    }

    public SelectOptionVO(String name, boolean isSelected)
    {
        this.name = name;
        this.isSelected = isSelected;
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
    public boolean isSelected()
    {
        return isSelected;
    }

    public void setSelected(boolean isSelected)
    {
        this.isSelected = isSelected;
    }

}
