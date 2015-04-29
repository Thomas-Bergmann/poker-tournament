package de.hatoka.tournament.internal.business;

import de.hatoka.common.capi.business.Money;
import de.hatoka.tournament.capi.business.CompetitorBO;
import de.hatoka.tournament.capi.entities.HistoryEntryType;

/**
 * This interface is used by game game implementation to set internal data of a competitor
 */
public interface CashGameCompetitor extends CompetitorBO
{
    void setInPlay(Money amount);

    void setActive(boolean active);

    void setResult(Money amount);

    void createEntry(HistoryEntryType type, Money amount);
}
