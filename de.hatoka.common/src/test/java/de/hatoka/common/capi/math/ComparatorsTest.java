package de.hatoka.common.capi.math;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.junit.jupiter.api.Test;

public class ComparatorsTest
{
    private static final List<String> STRING_START = Arrays.asList("A", null, "D", "B");
    private static final List<String> STRING_EXPECTED = Arrays.asList(null, "A", "B", "D");
    private static final List<Boolean> BOOLEAN_START = Arrays.asList(true, null, false);
    private static final List<Boolean> BOOLEAN_EXPECTED = Arrays.asList(null, false, true);

    @Test
    public void testStringSorting()
    {
        assertEquals(STRING_EXPECTED,
                        STRING_START.stream().sorted(Comparators.STRING).collect(Collectors.toList()), "null sorting");
    }

    @Test
    public void testBooleanSorting()
    {
        assertEquals(BOOLEAN_EXPECTED,
                        BOOLEAN_START.stream().sorted(Comparators.BOOLEAN).collect(Collectors.toList()), "money sorting");
    }
}
