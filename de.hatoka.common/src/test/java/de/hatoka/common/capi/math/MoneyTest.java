package de.hatoka.common.capi.math;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Currency;
import java.util.List;
import java.util.stream.Collectors;

import org.junit.jupiter.api.Test;

public class MoneyTest
{
    public static final Currency EUR = CurrencyConstants.EUR;
    public static final Currency USD = CurrencyConstants.USD;

    private static final BigDecimal BIG_DECIMAL_1_19 = new BigDecimal("1.19");
    private static final BigDecimal BIG_DECIMAL_TEN_SCALE = new BigDecimal("1E1");
    private static final Money UNDER_TEST_ONE = Money.valueOf(BigDecimal.ONE, EUR);
    private static final Money MONEY_1_19_EUR = Money.valueOf(BIG_DECIMAL_1_19, EUR);
    private static final List<Money> MONEY_START = Arrays.asList(Money.valueOf("-5 EUR"), Money.valueOf("-15 EUR"),
                    Money.valueOf("20 EUR"));
    private static final List<Money> MONEY_EXPECTED = Arrays.asList(Money.valueOf("-15 EUR"), Money.valueOf("-5 EUR"),
                    Money.valueOf("20 EUR"));

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
        assertEquals(new BigDecimal("0.840336"), UNDER_TEST_ONE.divide(BIG_DECIMAL_1_19).getAmount());
        assertEquals(new BigDecimal("1.6667"),
                        Money.valueOf(BIG_DECIMAL_TEN_SCALE, EUR).divide(new BigDecimal(6)).getAmount());
    }

    @Test
    public void testEquals()
    {
        assertEquals(MONEY_1_19_EUR.hashCode(), MONEY_1_19_EUR.hashCode());
        assertEquals(MONEY_1_19_EUR, MONEY_1_19_EUR);
        assertEquals(MONEY_1_19_EUR, Money.valueOf(new BigDecimal("1.190"), EUR));
        Money nothing = Money.valueOf(BigDecimal.ZERO, USD);
        assertTrue(nothing.equals(nothing));
        Money nothingInEUR = Money.valueOf(BigDecimal.ZERO, EUR);
        assertTrue(nothing.equals(nothingInEUR));
    }

    @Test
    public void testNonEquals()
    {
        assertFalse(MONEY_1_19_EUR.equals(Money.valueOf(BIG_DECIMAL_1_19, USD)));
        assertFalse(Money.valueOf(new BigDecimal("700.01"), EUR).equals(MONEY_1_19_EUR));
    }

    @Test
    public void testMulitiply()
    {
        assertEquals(BIG_DECIMAL_1_19, UNDER_TEST_ONE.multiply(BIG_DECIMAL_1_19).getAmount());
    }

    @Test
    public void testMoneySorting()
    {
        assertEquals(MONEY_EXPECTED, MONEY_START.stream().sorted(Money.COMPARATOR).collect(Collectors.toList()),
                        "money sorting");
    }

    @Test
    public void testToString()
    {
        // toString must convertible to Money
        assertEquals(BIG_DECIMAL_1_19, Money.valueOf(Money.valueOf(BIG_DECIMAL_1_19, USD).toString()).getAmount());
    }
}
