package de.hatoka.common.capi.app.model;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlTransient;


public class SelectVO
{
    @XmlElement(name = "option")
    private List<SelectOptionVO> options = new ArrayList<>();

    @XmlTransient
    public List<SelectOptionVO> getOptions()
    {
        return options;
    }

    public void setOptions(List<SelectOptionVO> options)
    {
        this.options = options;
    }
}
