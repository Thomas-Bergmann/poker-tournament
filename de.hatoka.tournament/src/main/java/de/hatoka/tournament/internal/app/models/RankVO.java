package de.hatoka.tournament.internal.app.models;

import java.math.BigDecimal;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlTransient;

import de.hatoka.tournament.capi.business.RankBO;

public class RankVO
{
    @XmlAttribute
    private String id;
    @XmlAttribute
    private int firstPosition;
    @XmlAttribute
    private Integer lastPosition;
    @XmlAttribute
    private BigDecimal percentage; // 0.5 for 50%
    @XmlAttribute
    private BigDecimal amountPerPlayer;
    @XmlAttribute
    private BigDecimal amount;

    public RankVO()
    {

    }

    public RankVO(RankBO rank)
    {
        id = rank.getID();
        firstPosition = rank.getFirstPosition();
        lastPosition = rank.getLastPosition();
        percentage = rank.getPercentage();
        amountPerPlayer = rank.getAmountPerPlayer().getAmount();
        amount = rank.getAmount().getAmount();
    }

    public RankVO(String id)
    {
        this.id = id;
    }

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

    @XmlAttribute
    public boolean isPercentageFilled()
    {
        return percentage != null && !percentage.equals(BigDecimal.ZERO);
    }
}
