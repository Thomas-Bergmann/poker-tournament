package de.hatoka.common.capi.business;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

import java.math.BigDecimal;
import java.util.Currency;

import org.junit.Test;

public class MoneyTest
{
    public static final Currency EUR = CurrencyConstants.EUR;
    public static final Currency USD = CurrencyConstants.USD;

    private static final BigDecimal BIG_DECIMAL_0_8403 = new BigDecimal("0.8403");
    private static final BigDecimal BIG_DECIMAL_1_19 = new BigDecimal("1.19");
    private static final Money UNDER_TEST_ONE = new Money(BigDecimal.ONE, EUR);

    @Test
    public void testAdd()
    {
        assertEquals(new BigDecimal("2"), UNDER_TEST_ONE.add(UNDER_TEST_ONE).getAmount());
        assertEquals(UNDER_TEST_ONE, UNDER_TEST_ONE.add(Money.NOTHING));
        assertEquals(UNDER_TEST_ONE, Money.NOTHING.add(UNDER_TEST_ONE));
    }

    @Test
    public void testDivide()
    {
        assertEquals(BIG_DECIMAL_0_8403, UNDER_TEST_ONE.divide(BIG_DECIMAL_1_19).getAmount());
    }

    @Test
    public void testEquals()
    {
        assertEquals(new Money(BIG_DECIMAL_1_19, EUR).hashCode(), new Money(BIG_DECIMAL_1_19, EUR).hashCode());
        assertEquals(new Money(BIG_DECIMAL_1_19, EUR), new Money(BIG_DECIMAL_1_19, EUR));
        assertEquals(new Money(BIG_DECIMAL_1_19, EUR), new Money(new BigDecimal("1.190"), EUR));
    }

    @Test
    public void testNonEquals()
    {
        assertFalse(new Money(BIG_DECIMAL_1_19, EUR).equals(new Money(BIG_DECIMAL_1_19, USD)));
        assertFalse(new Money(new BigDecimal("700.01"), EUR).equals(new Money(BIG_DECIMAL_1_19, EUR)));
    }

    @Test
    public void testMulitiply()
    {
        assertEquals(BIG_DECIMAL_1_19, UNDER_TEST_ONE.multiply(BIG_DECIMAL_1_19).getAmount());
    }
}
