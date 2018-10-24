package de.hatoka.player.internal.business;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import de.hatoka.common.capi.math.Money;
import de.hatoka.common.capi.persistence.MoneyPOConverter;
import de.hatoka.player.capi.business.HistoryEntryBO;
import de.hatoka.player.capi.business.PlayerBO;
import de.hatoka.player.capi.types.HistoryEntryType;
import de.hatoka.player.internal.persistence.HistoryPO;

@Component
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class HistoryEntryBOImpl implements HistoryEntryBO
{
    @Autowired
    private PlayerBOFactory playerFactory;
    private final HistoryPO historyPO;

    public HistoryEntryBOImpl(HistoryPO historyPO)
    {
        this.historyPO = historyPO;
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
        return historyPO.getType();
    }

    @Override
    public Money getAmount()
    {
        return MoneyPOConverter.valueOf(historyPO.getAmount());
    }

    @Override
    public PlayerBO getPlayer()
    {
        return playerFactory.get(historyPO.getPlayerRef()).get();
    }

    @Override
    public String getGameRef()
    {
        return historyPO.getGameRef();
    }

    @Override
    public String toString()
    {
        return historyPO.toString();
    }

}
