package de.hatoka.cashgame.internal.remote.model;

import de.hatoka.common.capi.math.Money;

public class CashGameInfoRO
{
    private Money average;
    private Money sumInPlay;

    public Money getAverage()
    {
        return average;
    }

    public Money getSumInPlay()
    {
        return sumInPlay;
    }

    public void setAverage(Money average)
    {
        this.average = average;
    }

    public void setSumInPlay(Money sumInPlay)
    {
        this.sumInPlay = sumInPlay;
    }
}
