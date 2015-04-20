package de.hatoka.common.capi.business;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.junit.Assert;
import org.junit.Test;

import de.hatoka.common.capi.resource.LocalizationConstants;

public class DateTest
{
    private static final long DATE_LONG = 1353829555000L;

    @Test
    public void testDateFormat() throws Exception
    {
        Date date = new SimpleDateFormat(LocalizationConstants.XML_DATEFORMAT_SECONDS).parse("2012-11-25T07:45:55Z");
        Assert.assertEquals("compare date", DATE_LONG, date.getTime());
    }
}
