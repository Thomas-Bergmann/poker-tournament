package de.hatoka.common.capi.math;

import java.math.BigDecimal;

/**
 * Represents scale for unit of measurement. E.g. kilo (without the gram)
 */
public enum UnitScale
{
    ONE("", BigDecimal.ONE),
    MILLI("m", BigDecimalConstants.MILLI),
    CENTI("c", BigDecimalConstants.CENTI),
    KILO("k", BigDecimalConstants.THOUSAND),
    MEGA("M", BigDecimalConstants.MILLION);

    private final BigDecimal baseValue;
    private final String abbreviation;

    /**
     *
     * @param abbreviation
     * @param baseValue
     */
    private UnitScale(String abbreviation, BigDecimal baseValue)
    {
        this.baseValue = baseValue;
        this.abbreviation = abbreviation;
    }

    public BigDecimal getBaseValue()
    {
        return baseValue;
    }

    @Override
    public String toString()
    {
        return abbreviation;
    }
}
