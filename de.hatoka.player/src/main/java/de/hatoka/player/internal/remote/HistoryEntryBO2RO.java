package de.hatoka.player.internal.remote;

import org.springframework.stereotype.Component;

import de.hatoka.player.capi.business.HistoryEntryBO;
import de.hatoka.player.capi.remote.HistoryEntryRO;

@Component
public class HistoryEntryBO2RO
{
    public HistoryEntryRO apply(HistoryEntryBO historyBO)
    {
        HistoryEntryRO result = new HistoryEntryRO();
        result.setPlayerName(historyBO.getPlayer().getName());
        result.setType(historyBO.getType());
        result.setDate(historyBO.getDate());
        result.setAmount(historyBO.getAmount());
        return result;
    }
}
