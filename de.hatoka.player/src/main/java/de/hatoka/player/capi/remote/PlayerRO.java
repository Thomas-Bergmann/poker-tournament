package de.hatoka.player.capi.remote;

import com.fasterxml.jackson.annotation.JsonProperty;

public class PlayerRO
{
    @JsonProperty("refLocal")
    private String refLocal;
    @JsonProperty("refGlobal")
    private String refGlobal;
    private PlayerDataRO data;
    private PlayerInfoRO info;

    public PlayerRO()
    {
    }


    public PlayerDataRO getData()
    {
        return data;
    }

    public void setData(PlayerDataRO data)
    {
        this.data = data;
    }

    public PlayerInfoRO getInfo()
    {
        return info;
    }

    public void setInfo(PlayerInfoRO info)
    {
        this.info = info;
    }


    public String getRefLocal()
    {
        return refLocal;
    }


    public void setRefLocal(String refLocal)
    {
        this.refLocal = refLocal;
    }


    public String getRefGlobal()
    {
        return refGlobal;
    }


    public void setRefGlobal(String refGlobal)
    {
        this.refGlobal = refGlobal;
    }
}
