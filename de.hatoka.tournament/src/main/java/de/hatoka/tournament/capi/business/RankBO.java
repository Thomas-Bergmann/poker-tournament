package de.hatoka.tournament.capi.business;

import java.math.BigDecimal;

import de.hatoka.common.capi.business.Money;

public interface RankBO
{
    /**
     * @return the identifier (artificial key)
     */
    String getID();

    Integer getFirstPosition();

    Integer getLastPosition();

    BigDecimal getPercentage();

    Money getAmountPerPlayer();

    Money getAmount();
}
