package de.hatoka.player.internal.business;

import org.springframework.beans.factory.annotation.Lookup;
import org.springframework.stereotype.Component;

import de.hatoka.player.capi.business.HistoryEntryBO;
import de.hatoka.player.internal.persistence.HistoryPO;

@Component
public class HistoryBOFactoryImpl implements HistoryBOFactory
{
    @Lookup
    @Override
    public HistoryEntryBO get(HistoryPO po)
    {
        // done by @Lookup
        return null;
    }
}
