package de.hatoka.tournament.internal.app.models;

import java.math.BigDecimal;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlTransient;

public class RankVO
{
    @XmlAttribute
    private String id;
    @XmlAttribute
    private int firstPosition;
    @XmlAttribute
    private Integer lastPosition;
    @XmlAttribute
    private boolean isLastPositionCalculated = true;
    @XmlAttribute
    private BigDecimal percentage; // 0.5 for 50%
    @XmlAttribute
    private boolean isPercentageCalculated = false;
    @XmlAttribute
    private BigDecimal amountPerPlayer;
    @XmlAttribute
    private boolean isAmountPerPlayerCalculated = false;
    @XmlAttribute
    private BigDecimal amount;

    @XmlTransient
    public String getId()
    {
        return id;
    }
    public void setId(String id)
    {
        this.id = id;
    }
    @XmlTransient
    public int getFirstPosition()
    {
        return firstPosition;
    }
    public void setFirstPosition(int firstPosition)
    {
        this.firstPosition = firstPosition;
    }
    @XmlTransient
    public Integer getLastPosition()
    {
        return lastPosition;
    }
    public void setLastPosition(Integer lastPosition)
    {
        this.lastPosition = lastPosition;
    }
    @XmlTransient
    public BigDecimal getPercentage()
    {
        return percentage;
    }
    public void setPercentage(BigDecimal percentage)
    {
        this.percentage = percentage;
    }
    @XmlTransient
    public BigDecimal getAmountPerPlayer()
    {
        return amountPerPlayer;
    }
    public void setAmountPerPlayer(BigDecimal amountPerPlayer)
    {
        this.amountPerPlayer = amountPerPlayer;
    }
    @XmlTransient
    public BigDecimal getAmount()
    {
        return amount;
    }
    public void setAmount(BigDecimal amount)
    {
        this.amount = amount;
    }

    @XmlTransient
    public boolean isLastPositionCalculated()
    {
        return isLastPositionCalculated;
    }
    public void setLastPositionCalculated(boolean isLastPositionCalculated)
    {
        this.isLastPositionCalculated = isLastPositionCalculated;
    }
    @XmlTransient
    public boolean isPercentageCalculated()
    {
        return isPercentageCalculated;
    }
    public void setPercentageCalculated(boolean isPercentageCalculated)
    {
        this.isPercentageCalculated = isPercentageCalculated;
    }
    @XmlTransient
    public boolean isAmountPerPlayerCalculated()
    {
        return isAmountPerPlayerCalculated;
    }
    public void setAmountPerPlayerCalculated(boolean isAmountPerPlayerCalculated)
    {
        this.isAmountPerPlayerCalculated = isAmountPerPlayerCalculated;
    }
}
