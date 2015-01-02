package de.hatoka.common.capi.business;

import static org.junit.Assert.assertEquals;

import java.math.BigDecimal;

import org.junit.Test;

public class QuantityTest
{
    private static final Quantity ONE_KILO_GRAM = new Quantity(BigDecimal.ONE, UnitScale.KILO, Unit.GRAM);
    private static final Quantity SEVEN_HUNDRED_GRAM = new Quantity(new BigDecimal("700"), Unit.GRAM);
    private static final Quantity SEVEN_HUNDRED_KILO_GRAM = new Quantity(new BigDecimal("700"), UnitScale.KILO,
                    Unit.GRAM);
    private static final Quantity SEVEN_HUNDRED_LITER = new Quantity(new BigDecimal("700"), Unit.LITER);

    @Test
    public void testDivide()
    {
        assertEquals(new BigDecimal("0.7"), SEVEN_HUNDRED_GRAM.divide(ONE_KILO_GRAM));
        assertEquals(new BigDecimal("700"), SEVEN_HUNDRED_KILO_GRAM.divide(ONE_KILO_GRAM));
    }

    @Test
    public void testEquals()
    {
        assertEquals(new Quantity(new BigDecimal("700.00"), Unit.GRAM).hashCode(), new Quantity(new BigDecimal("700"), Unit.GRAM).hashCode());
        assertEquals(new Quantity(new BigDecimal("700.00"), Unit.GRAM), new Quantity(new BigDecimal("700"), Unit.GRAM));
    }

    @Test
    public void testToString()
    {
        assertEquals("700g", SEVEN_HUNDRED_GRAM.toString());
        assertEquals("1kg", ONE_KILO_GRAM.toString());
        assertEquals("1", Quantity.ONE_PCS.toString());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testWrongUnit()
    {
        SEVEN_HUNDRED_LITER.divide(ONE_KILO_GRAM);
    }
}
