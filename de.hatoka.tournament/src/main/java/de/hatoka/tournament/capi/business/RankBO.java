package de.hatoka.tournament.capi.business;

import java.math.BigDecimal;

import de.hatoka.common.capi.math.Money;

public interface RankBO
{
    void remove();

    /**
     * @return first player position for the rank (e.g. 6)
     */
    Integer getFirstPosition();

    /**
     * @return last player position for the rank (e.g. 10)
     */
    Integer getLastPosition();

    /**
     * @return percentage of whole price (0.5 for 50%) for the rank (e.g. 20% for the rank)
     */
    BigDecimal getPercentage();

    /**
     * @return amount per players will get same price (e.g. 10 Euro in case place 6 to 10 get the same price and 50 Euro for the rank)
     */
    Money getAmountPerPlayer();

    /**
     * @return calculated or fix amount for the rank (e.g. 50 Euro for place 6 to 10) or null in case not calculated yet
     */
    Money getAmount();
}
