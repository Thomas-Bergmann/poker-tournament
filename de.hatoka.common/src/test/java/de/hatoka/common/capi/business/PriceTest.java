package de.hatoka.common.capi.business;

import static org.junit.Assert.assertEquals;

import java.math.BigDecimal;

import org.junit.Test;

public class PriceTest
{
    private static final Quantity ONE_KILO_GRAM = new Quantity(BigDecimal.ONE, UnitScale.KILO, Unit.GRAM);
    private static final Quantity SEVEN_HUNDRED_GRAM = new Quantity(new BigDecimal("700"), Unit.GRAM);

    private static final BigDecimal BIG_DECIMAL_0_84 = new BigDecimal("0.84");
    private static final BigDecimal BIG_DECIMAL_1_19 = new BigDecimal("1.19");
    private static final Price UNDER_TEST_ONE = new Price(BigDecimal.ONE, CurrencyConstants.EUR);

    private static final Price PRICE_10_EUR_PER_KILO = new Price(Money.valueOf(BigDecimal.TEN, CurrencyConstants.EUR), ONE_KILO_GRAM);
    private static final Price PRICE_7_EUR_PER_SEVEN_HUNDED_GRAM = new Price(Money.valueOf(new BigDecimal("7"), CurrencyConstants.EUR), SEVEN_HUNDRED_GRAM);

    @Test
    public void testDivide()
    {
        assertEquals(BIG_DECIMAL_0_84, UNDER_TEST_ONE.divide(BIG_DECIMAL_1_19).getMoney().round().getAmount());
    }

    @Test
    public void testKiloMultiply()
    {
        assertEquals(PRICE_7_EUR_PER_SEVEN_HUNDED_GRAM, PRICE_10_EUR_PER_KILO.getPriceFor(SEVEN_HUNDRED_GRAM));
    }

    @Test
    public void testMulitiply()
    {
        assertEquals(BIG_DECIMAL_1_19, UNDER_TEST_ONE.multiply(BIG_DECIMAL_1_19).getMoney().round().getAmount());
    }
}
