package de.hatoka.common.capi.locale;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.junit.jupiter.api.Test;

public class DateTest
{
    private static final long DATE_LONG = 1353829555000L;

    @Test
    public void testDateFormat() throws Exception
    {
        Date date = new SimpleDateFormat(LocalizationConstants.XML_DATEFORMAT_SECONDS).parse("2012-11-25T07:45:55Z");
        assertEquals(DATE_LONG, date.getTime(), "same date after toString and parse");
    }
}
