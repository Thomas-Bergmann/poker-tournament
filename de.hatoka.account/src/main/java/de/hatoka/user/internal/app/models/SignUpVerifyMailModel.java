package de.hatoka.user.internal.app.models;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class SignUpVerifyMailModel
{
    private String link;

    public String getLink()
    {
        return link;
    }

    public void setLink(String link)
    {
        this.link = link;
    }
}
