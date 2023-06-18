package de.hatoka.player.capi.remote;

import java.util.Date;

import de.hatoka.common.capi.math.Money;
import de.hatoka.player.capi.types.HistoryEntryType;

public class HistoryEntryRO
{
    private String playerName;
    private HistoryEntryType entryType;
    private Date date;
    private Money amount;

    public HistoryEntryRO()
    {
    }

    public String getPlayerName()
    {
        return playerName;
    }

    public void setPlayerName(String playerName)
    {
        this.playerName = playerName;
    }

    public Date getDate()
    {
        return date;
    }

    public void setDate(Date date)
    {
        this.date = date;
    }

    public Money getAmount()
    {
        return amount;
    }

    public void setAmount(Money amount)
    {
        this.amount = amount;
    }

    public HistoryEntryType getType()
    {
        return entryType;
    }

    public void setType(HistoryEntryType entry)
    {
        this.entryType = entry;
    }
}
