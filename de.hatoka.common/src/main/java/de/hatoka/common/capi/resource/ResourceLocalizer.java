package de.hatoka.common.capi.resource;

import java.text.DateFormat;
import java.text.MessageFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import de.hatoka.common.capi.app.xslt.Localizer;

public class ResourceLocalizer implements Localizer
{
    private final LocalizationBundle bundle;

    public ResourceLocalizer(LocalizationBundle bundle)
    {
        this.bundle = bundle;
    }

    @Override
    public String formatDate(String dateString)
    {
        if (dateString == null || dateString.isEmpty())
        {
            return "";
        }
        Date date = parseDate(dateString);
        return DateFormat.getDateInstance(DateFormat.SHORT, bundle.getLocal()).format(date);
    }

    @Override
    public String formatDateTime(String dateString)
    {
        if (dateString == null || dateString.isEmpty())
        {
            return "";
        }
        Date date = parseDate(dateString);

        DateFormat dateTimeFormatter = DateFormat.getDateTimeInstance(DateFormat.SHORT, DateFormat.SHORT, bundle.getLocal());
        dateTimeFormatter.setTimeZone(bundle.getTimeZone());
        return dateTimeFormatter.format(date);
    }

    private Date parseDate(String dateString)
    {
        // convert xml date format to target
        // 2014-11-25T09:45:55.624+01:00
        // 2015-03-01T00:00:00+01:00
        // 12345678901234567890123456789
        try
        {
            if (dateString.length() == 25)
            {
                return new SimpleDateFormat(LocalizationConstants.XML_DATEFORMAT_SECONDS).parse(dateString);
            }
            return new SimpleDateFormat(LocalizationConstants.XML_DATEFORMAT).parse(dateString);
        }
        catch(ParseException e)
        {
            throw new IllegalArgumentException("Illegal source date string: '" + dateString + "'", e);
        }
    }

    @Override
    public String getText(String key, String defaultText, Object... arguments)
    {
        String pattern = bundle.getText(key, null);
        if (pattern == null)
        {
            return defaultText;
        }
        MessageFormat messageFormat = new MessageFormat(pattern, bundle.getLocal());
        return messageFormat.format(arguments, new StringBuffer(), null).toString();
    }

}
