package de.hatoka.group.capi.remote;

import com.fasterxml.jackson.annotation.JsonProperty;

public class GroupRO
{
    @JsonProperty("refLocal")
    private String refLocal;
    @JsonProperty("refGlobal")
    private String refGlobal;
    private GroupDataRO data;
    private GroupInfoRO info;

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

    public GroupDataRO getData()
    {
        return data;
    }

    public void setData(GroupDataRO data)
    {
        this.data = data;
    }

    public GroupInfoRO getInfo()
    {
        return info;
    }

    public void setInfo(GroupInfoRO info)
    {
        this.info = info;
    }
}
