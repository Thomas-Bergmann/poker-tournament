package de.hatoka.tournament.internal.app.models;

import java.util.Date;

import javax.xml.bind.annotation.XmlAttribute;

import de.hatoka.common.capi.app.model.MoneyVO;
import de.hatoka.common.capi.business.Money;
import de.hatoka.tournament.capi.entities.HistoryEntryType;
import de.hatoka.tournament.capi.entities.HistoryPO;

public class HistoryEntryVO
{
    private String playerName;
    private HistoryEntryType entryType;
    private Date date;
    private MoneyVO amount;

    public HistoryEntryVO()
    {
    }

    public HistoryEntryVO(HistoryPO historyPO)
    {
        this.playerName = historyPO.getPlayerPO().getName();
        this.entryType = HistoryEntryType.valueOf(historyPO.getActionKey());
        this.date = historyPO.getDate();
        this.amount = new MoneyVO(Money.getInstance(historyPO.getAmount()));
    }

    @XmlAttribute
    public String getActionKey()
    {
        return entryType.getTemplateKey();
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

    public MoneyVO getAmount()
    {
        return amount;
    }

    public void setAmount(MoneyVO amount)
    {
        this.amount = amount;
    }

    public HistoryEntryType getEntry()
    {
        return entryType;
    }

    public void setEntry(HistoryEntryType entry)
    {
        this.entryType = entry;
    }
}
