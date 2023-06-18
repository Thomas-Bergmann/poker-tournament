package de.hatoka.cashgame.internal.business;

import de.hatoka.cashgame.capi.business.CashGameBO;
import de.hatoka.cashgame.internal.persistence.CashGamePO;

public interface CashGameBOFactory
{
    CashGameBO get(CashGamePO tournamentPO);
}
