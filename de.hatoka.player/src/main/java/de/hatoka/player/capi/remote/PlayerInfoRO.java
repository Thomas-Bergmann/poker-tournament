package de.hatoka.player.capi.remote;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonProperty;

import de.hatoka.common.capi.math.Money;

public class PlayerInfoRO
{
    @JsonProperty("firstDate")
    private Date firstDate;
    @JsonProperty("lastWeek")
    private Money lastWeek;
    @JsonProperty("lastMonth")
    private Money lastMonth;
    @JsonProperty("lastYear")
    private Money lastYear;

    public Date getFirstDate()
    {
        return firstDate;
    }
    public void setFirstDate(Date firstDate)
    {
        this.firstDate = firstDate;
    }
    public Money getLastWeek()
    {
        return lastWeek;
    }
    public void setLastWeek(Money lastWeek)
    {
        this.lastWeek = lastWeek;
    }
    public Money getLastMonth()
    {
        return lastMonth;
    }
    public void setLastMonth(Money lastMonth)
    {
        this.lastMonth = lastMonth;
    }
    public Money getLastYear()
    {
        return lastYear;
    }
    public void setLastYear(Money lastYear)
    {
        this.lastYear = lastYear;
    }
}
