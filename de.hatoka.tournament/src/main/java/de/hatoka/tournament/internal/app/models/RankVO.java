package de.hatoka.tournament.internal.app.models;

import java.math.BigDecimal;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlTransient;

import de.hatoka.common.capi.app.model.MoneyVO;
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
    private MoneyVO amountPerPlayer;
    private MoneyVO amount;

    public RankVO()
    {

    }

    public RankVO(RankBO rank)
    {
        id = rank.getID();
        firstPosition = rank.getFirstPosition();
        lastPosition = rank.getLastPosition();
        percentage = rank.getPercentage();
        setAmountPerPlayer(new MoneyVO(rank.getAmountPerPlayer()));
        setAmount(new MoneyVO(rank.getAmount()));
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

    @XmlAttribute
    public boolean isPercentageFilled()
    {
        return percentage != null && !percentage.equals(BigDecimal.ZERO);
    }

    public MoneyVO getAmountPerPlayer()
    {
        return amountPerPlayer;
    }

    public void setAmountPerPlayer(MoneyVO amountPerPlayer)
    {
        this.amountPerPlayer = amountPerPlayer;
    }

    public MoneyVO getAmount()
    {
        return amount;
    }

    public void setAmount(MoneyVO amount)
    {
        this.amount = amount;
    }
}
