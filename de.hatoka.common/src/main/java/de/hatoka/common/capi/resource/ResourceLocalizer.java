package de.hatoka.common.capi.resource;

import java.text.MessageFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import de.hatoka.common.capi.app.xslt.Localizer;

public class ResourceLocalizer implements Localizer
{
    private final LocalizationBundle bundle;
    private final String dateFormat;

    public ResourceLocalizer(LocalizationBundle bundle)
    {
        this(bundle, "yyyy/MM/dd");
    }

    public ResourceLocalizer(LocalizationBundle bundle, String dateFormat)
    {
        this.bundle = bundle;
        this.dateFormat = dateFormat;
    }

    @Override
    public String formatDate(String dateString)
    {
        // 2014-11-25T09:45:55.624+01:00
        Date date;
        try
        {
            date = new SimpleDateFormat(LocalizationConstants.XML_DATEFORMAT).parse(dateString);
        }
        catch(ParseException e)
        {
            return dateString;
        }
        return new SimpleDateFormat(dateFormat).format(date);
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
