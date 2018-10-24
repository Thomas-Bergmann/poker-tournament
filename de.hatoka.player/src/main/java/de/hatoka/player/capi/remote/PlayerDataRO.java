package de.hatoka.player.capi.remote;

import com.fasterxml.jackson.annotation.JsonProperty;

public class PlayerDataRO
{
    @JsonProperty("name")
    private String name;
    @JsonProperty("eMail")
    private String eMail;

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public String geteMail()
    {
        return eMail;
    }

    public void seteMail(String eMail)
    {
        this.eMail = eMail;
    }
}
