package de.hatoka.tournament.internal.business;

import de.hatoka.common.capi.business.Money;
import de.hatoka.tournament.capi.business.CashGameCompetitorBO;
import de.hatoka.tournament.capi.types.HistoryEntryType;

/**
 * This interface is used by game game implementation to set internal data of a competitor
 */
public interface ICompetitor extends CashGameCompetitorBO
{
    void setInPlay(Money amount);

    void setInactive();

    void setResult(Money amount);

    void createEntry(HistoryEntryType type, Money amount);
}
