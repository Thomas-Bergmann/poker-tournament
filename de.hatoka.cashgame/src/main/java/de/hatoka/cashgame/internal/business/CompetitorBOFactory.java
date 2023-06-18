package de.hatoka.cashgame.internal.business;

import de.hatoka.cashgame.capi.business.CashGameBO;
import de.hatoka.cashgame.internal.persistence.CashGameCompetitorPO;

public interface CompetitorBOFactory
{
    ICompetitorBO get(CashGameCompetitorPO po, CashGameBO cashGame);
}
