package de.hatoka.common.capi.encoding;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

import java.io.IOException;
import java.math.BigDecimal;

import org.junit.Test;

import de.hatoka.common.capi.entities.MoneyPO;

public class StringSerializerTest
{
    private static final String TEST_STRING_1 = "simple";

    StringSerializer<String> UNDER_TEST = new StringSerializer<String>(String.class);
    StringSerializer<MoneyPO> UNDER_TEST_PO = new StringSerializer<MoneyPO>(MoneyPO.class);

    @Test
    public void testMoneyPO() throws IOException
    {
        MoneyPO po = new MoneyPO(BigDecimal.TEN, "USD");
        String serial = UNDER_TEST_PO.serialize(po);
        MoneyPO result = UNDER_TEST_PO.deserialize(serial);
        assertEquals("first requirement restored object must be equal", po, result);
        assertFalse("second requirement object is not the same", po == result);
    }

    @Test
    public void testPerformance() throws IOException
    {
        for(int price = 0; price < 10000; price++)
        {
            MoneyPO value = new MoneyPO(BigDecimal.valueOf(price), "USD");
            String serial = UNDER_TEST_PO.serialize(value);
            UNDER_TEST_PO.deserialize(serial);
        }
    }

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
