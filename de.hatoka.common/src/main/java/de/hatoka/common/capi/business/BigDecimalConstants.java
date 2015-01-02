package de.hatoka.common.capi.business;

import java.math.BigDecimal;

public final class BigDecimalConstants
{
    public static final BigDecimal MILLI = new BigDecimal("0.001");
    public static final BigDecimal CENTI = MILLI.multiply(BigDecimal.TEN);
    public static final BigDecimal DECI = CENTI.multiply(BigDecimal.TEN);
    public static final BigDecimal HUNDRED = BigDecimal.TEN.multiply(BigDecimal.TEN);
    public static final BigDecimal THOUSAND = HUNDRED.multiply(BigDecimal.TEN);
    public static final BigDecimal MILLION = THOUSAND.multiply(THOUSAND);

    private BigDecimalConstants()
    {

    }
}
