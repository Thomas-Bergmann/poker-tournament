package de.hatoka.player.internal.business;

import de.hatoka.player.capi.business.HistoryEntryBO;
import de.hatoka.player.internal.persistence.HistoryPO;

public interface HistoryBOFactory
{
    HistoryEntryBO get(HistoryPO po);
}
