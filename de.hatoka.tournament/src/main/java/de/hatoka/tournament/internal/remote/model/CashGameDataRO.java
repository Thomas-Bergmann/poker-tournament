package de.hatoka.tournament.internal.remote.model;

import java.util.Date;

import de.hatoka.common.capi.math.Money;

public class CashGameDataRO
{
    private Date date;
    private Money buyIn;

    public Money getBuyIn()
    {
        return buyIn;
    }

    public Date getDate()
    {
        return date;
    }

    public void setBuyIn(Money buyIn)
    {
        this.buyIn = buyIn;
    }

    public void setDate(Date date)
    {
        this.date = date;
    }
}
