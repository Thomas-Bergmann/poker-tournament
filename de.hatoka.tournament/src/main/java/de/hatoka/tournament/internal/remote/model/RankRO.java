package de.hatoka.tournament.internal.remote.model;

import java.math.BigDecimal;

import de.hatoka.common.capi.math.Money;

public class RankRO
{
    private int firstPosition;
    private Integer lastPosition;
    private BigDecimal percentage; // 0.5 for 50%
    private Money amountPerPlayer;
    private Money amount;

    public RankRO()
    {
    }

    public int getFirstPosition()
    {
        return firstPosition;
    }

    public void setFirstPosition(int firstPosition)
    {
        this.firstPosition = firstPosition;
    }

    public Integer getLastPosition()
    {
        return lastPosition;
    }

    public void setLastPosition(Integer lastPosition)
    {
        this.lastPosition = lastPosition;
    }

    public BigDecimal getPercentage()
    {
        return percentage;
    }

    public void setPercentage(BigDecimal percentage)
    {
        this.percentage = percentage;
    }

    public boolean isPercentageFilled()
    {
        return percentage != null && !percentage.equals(BigDecimal.ZERO);
    }

    public Money getAmountPerPlayer()
    {
        return amountPerPlayer;
    }

    public void setAmountPerPlayer(Money amountPerPlayer)
    {
        this.amountPerPlayer = amountPerPlayer;
    }

    public Money getAmount()
    {
        return amount;
    }

    public void setAmount(Money amount)
    {
        this.amount = amount;
    }
}
