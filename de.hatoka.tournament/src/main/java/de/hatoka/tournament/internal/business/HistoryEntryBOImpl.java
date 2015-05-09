package de.hatoka.tournament.internal.business;

import java.util.Date;

import de.hatoka.common.capi.business.Money;
import de.hatoka.tournament.capi.business.HistoryEntryBO;
import de.hatoka.tournament.capi.business.PlayerBO;
import de.hatoka.tournament.capi.business.TournamentBusinessFactory;
import de.hatoka.tournament.capi.entities.HistoryPO;
import de.hatoka.tournament.capi.types.HistoryEntryType;

public class HistoryEntryBOImpl implements HistoryEntryBO
{
    private final HistoryPO historyPO;
    private final TournamentBusinessFactory factory;

    public HistoryEntryBOImpl(HistoryPO historyPO, TournamentBusinessFactory factory)
    {
        this.historyPO = historyPO;
        this.factory = factory;
    }

    @Override
    public int hashCode()
    {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((historyPO == null) ? 0 : historyPO.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj)
    {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        HistoryEntryBOImpl other = (HistoryEntryBOImpl)obj;
        if (historyPO == null)
        {
            if (other.historyPO != null)
                return false;
        }
        else if (!historyPO.equals(other.historyPO))
            return false;
        return true;
    }

    @Override
    public Date getDate()
    {
        return historyPO.getDate();
    }

    @Override
    public HistoryEntryType getType()
    {
        return HistoryEntryType.valueOf(historyPO.getActionKey());
    }

    @Override
    public Money getAmount()
    {
        return Money.getInstance(historyPO.getAmount());
    }

    @Override
    public PlayerBO getPlayer()
    {
        return factory.getPlayerBO(historyPO.getPlayerPO());
    }

}
