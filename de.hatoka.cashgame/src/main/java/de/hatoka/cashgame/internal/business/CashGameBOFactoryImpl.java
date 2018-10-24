package de.hatoka.cashgame.internal.business;

import org.springframework.beans.factory.annotation.Lookup;
import org.springframework.stereotype.Component;

import de.hatoka.cashgame.capi.business.CashGameBO;
import de.hatoka.cashgame.internal.persistence.CashGamePO;

@Component
public class CashGameBOFactoryImpl implements CashGameBOFactory
{
    @Lookup
    @Override
    public CashGameBO get(CashGamePO po)
    {
        // done by @Lookup
        return null;
    }
}
