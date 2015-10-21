package de.hatoka.tournament.internal.business;

import de.hatoka.tournament.capi.business.CompetitorBO;
import de.hatoka.tournament.capi.business.TableBO;

/**
 * This interface is used by game game implementation to set internal data of a competitor
 */
public interface ITableBO extends TableBO
{
    void add(CompetitorBO competitorBO);
}
