package de.hatoka.common.capi.encoding;



import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.IOException;

import org.junit.jupiter.api.Test;

public class StringSerializerTest
{
    private static final String TEST_STRING_1 = "simple";

    StringSerializer<String> UNDER_TEST = new StringSerializer<String>(String.class);

    @Test
    public void testSimpleText() throws IOException
    {
        String serial = UNDER_TEST.serialize(TEST_STRING_1);
        String result = UNDER_TEST.deserialize(serial);
        // first requirement restored object must be equal
        assertEquals(TEST_STRING_1, result);
        // a string should not be converted
        assertEquals(TEST_STRING_1, serial);
    }
}
