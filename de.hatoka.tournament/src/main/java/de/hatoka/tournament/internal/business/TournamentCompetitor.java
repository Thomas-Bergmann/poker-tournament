package de.hatoka.tournament.internal.business;

import de.hatoka.common.capi.business.Money;
import de.hatoka.tournament.capi.business.CompetitorBO;
import de.hatoka.tournament.capi.entities.HistoryEntryType;

public interface TournamentCompetitor extends CompetitorBO
{

    void setActive(boolean active);

    void setResult(Money moneyResult);

    void createEntry(HistoryEntryType cashout, Money result);

}
