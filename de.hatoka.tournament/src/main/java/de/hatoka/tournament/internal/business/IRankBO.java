package de.hatoka.tournament.internal.business;

import de.hatoka.common.capi.math.Money;
import de.hatoka.tournament.capi.business.RankBO;

public interface IRankBO extends RankBO
{
    /**
     * Set amount for rank
     * @param amount for all players in thank rank
     */
    void setAmount(Money amount);
}
