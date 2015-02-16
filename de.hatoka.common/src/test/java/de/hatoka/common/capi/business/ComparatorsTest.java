package de.hatoka.common.capi.business;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.junit.Assert;
import org.junit.Test;

public class ComparatorsTest
{
    private static final List<String> STRING_START = Arrays.asList("A", null, "D", "B");
    private static final List<String> STRING_EXPECTED = Arrays.asList(null, "A", "B", "D");
    private static final List<Money> MONEY_START = Arrays.asList(Money.getInstance("-5 EUR"), Money.getInstance("-15 EUR"), Money.getInstance("20 EUR"));
    private static final List<Money> MONEY_EXPECTED = Arrays.asList(Money.getInstance("-15 EUR"), Money.getInstance("-5 EUR"), Money.getInstance("20 EUR"));
    private static final List<Boolean> BOOLEAN_START = Arrays.asList(true, null, false);
    private static final List<Boolean> BOOLEAN_EXPECTED = Arrays.asList(null, false, true);

    @Test
    public void testStringSorting()
    {
        Assert.assertEquals("null sorting", STRING_EXPECTED, STRING_START.stream().sorted(Comparators.STRING).collect(Collectors.toList()));
    }

    @Test
    public void testMoneySorting()
    {
        Assert.assertEquals("money sorting", MONEY_EXPECTED, MONEY_START.stream().sorted(Comparators.MONEY).collect(Collectors.toList()));
    }

    @Test
    public void testBooleanSorting()
    {
        Assert.assertEquals("money sorting", BOOLEAN_EXPECTED, BOOLEAN_START.stream().sorted(Comparators.BOOLEAN).collect(Collectors.toList()));
    }
}
