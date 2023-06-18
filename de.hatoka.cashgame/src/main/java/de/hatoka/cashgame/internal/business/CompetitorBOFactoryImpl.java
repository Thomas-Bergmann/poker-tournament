package de.hatoka.cashgame.internal.business;

import org.springframework.beans.factory.annotation.Lookup;
import org.springframework.stereotype.Component;

import de.hatoka.cashgame.capi.business.CashGameBO;
import de.hatoka.cashgame.internal.persistence.CashGameCompetitorPO;

@Component("CashGameCompetitorBOFactory")
public class CompetitorBOFactoryImpl implements CompetitorBOFactory
{
    @Override
    @Lookup
    public ICompetitorBO get(CashGameCompetitorPO po, CashGameBO cashGame)
    {
        // done by @Lookup
        return null;
    }
}
