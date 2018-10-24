package de.hatoka.tournament.internal.remote.model;

public class CashGameRO
{
    private String ref;
    private CashGameDataRO data;
    private CashGameInfoRO info;

    public String getRef()
    {
        return ref;
    }

    public void setRef(String ref)
    {
        this.ref = ref;
    }

    public CashGameDataRO getData()
    {
        return data;
    }

    public void setData(CashGameDataRO data)
    {
        this.data = data;
    }

    public CashGameInfoRO getInfo()
    {
        return info;
    }

    public void setInfo(CashGameInfoRO info)
    {
        this.info = info;
    }
}
