package de.hatoka.tournament.internal.remote.model;

import java.util.Date;

import de.hatoka.common.capi.math.Money;

public class TournamentRO
{
    private String name;
    private Date date;
    private Money buyIn;
    private Money reBuy;
    private Money average;
    private Money sumInPlay;
    private Integer competitorsSize;
    private Integer initialStack;
    private Integer largestTable;

    public TournamentRO()
    {
    }

    public Money getAverage()
    {
        return average;
    }

    public Money getBuyIn()
    {
        return buyIn;
    }

    public Date getDate()
    {
        return date;
    }

    public String getName()
    {
        return name;
    }

    public Money getSumInPlay()
    {
        return sumInPlay;
    }

    public void setAverage(Money average)
    {
        this.average = average;
    }

    public void setBuyIn(Money buyIn)
    {
        this.buyIn = buyIn;
    }

    public void setDate(Date date)
    {
        this.date = date;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public void setSumInPlay(Money sumInPlay)
    {
        this.sumInPlay = sumInPlay;
    }

    public Integer getCompetitorsSize()
    {
        return competitorsSize;
    }

    public void setCompetitorsSize(Integer competitorsSize)
    {
        this.competitorsSize = competitorsSize;
    }

    public Integer getInitialStack()
    {
        return initialStack;
    }

    public void setInitialStack(Integer initialStack)
    {
        this.initialStack = initialStack;
    }

    public Integer getLargestTable()
    {
        return largestTable;
    }

    public void setLargestTable(Integer largestTable)
    {
        this.largestTable = largestTable;
    }

    public Money getReBuy()
    {
        return reBuy;
    }

    public void setReBuy(Money reBuy)
    {
        this.reBuy = reBuy;
    }
}
