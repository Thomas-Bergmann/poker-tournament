package de.hatoka.common.capi.app.model;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlTransient;

public class MessageVO
{
    @XmlAttribute
    private String messageKey;
    private Object[] parameter;

    public MessageVO()
    {
    }

    public MessageVO(String messageKey, Object... parameter)
    {
        this.messageKey = messageKey;
        this.parameter = parameter;
    }

    @XmlTransient
    public String getMessageKey()
    {
        return messageKey;
    }

    public void setMessageKey(String messageKey)
    {
        this.messageKey = messageKey;
    }

    public Object[] getParameter()
    {
        return parameter;
    }

    public void setParameter(Object[] parameter)
    {
        this.parameter = parameter;
    }

    @XmlAttribute
    public String getParameter1()
    {
        if (parameter.length < 1)
        {
            return null;
        }
        return parameter[0].toString();
    }

    @XmlAttribute
    public String getParameter2()
    {
        if (parameter.length < 2)
        {
            return null;
        }
        return parameter[1].toString();
    }

    @XmlAttribute
    public String getParameter3()
    {
        if (parameter.length < 3)
        {
            return null;
        }
        return parameter[2].toString();
    }

}
