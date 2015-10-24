package de.hatoka.common.capi.resource;

import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.MessageFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Currency;
import java.util.Date;

import org.slf4j.LoggerFactory;

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

    @Override
    public String formatTime(String dateString)
    {
        if (dateString == null || dateString.isEmpty())
        {
            return "";
        }
        Date date = parseDate(dateString);

        DateFormat dateTimeFormatter = DateFormat.getTimeInstance(DateFormat.SHORT, bundle.getLocal());
        dateTimeFormatter.setTimeZone(bundle.getTimeZone());
        return dateTimeFormatter.format(date);
    }

    private Date parseDate(String dateString)
    {
        // convert xml date format to target
        // 2014-11-25T09:45:55.624+01:00 (second try)
        // 2012-11-25T07:45:55Z
        // 2015-03-01T00:00:00+01:00
        // 12345678901234567890123456789
        try
        {
            if (dateString.length() == 25 || dateString.length() == 20)
            {
                return new SimpleDateFormat(LocalizationConstants.XML_DATEFORMAT_SECONDS).parse(dateString);
            }
            return new SimpleDateFormat(LocalizationConstants.XML_DATEFORMAT_MILLIS).parse(dateString);
        }
        catch(ParseException e)
        {
            LoggerFactory.getLogger(getClass()).error("Can't convert to date '{}'.", dateString);
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

    @Override
    public String formatDuration(String duration)
    {
        if (duration == null || duration.isEmpty())
        {
            return "";
        }
        try
        {
            int minutes = Integer.valueOf(duration);
            int hours = minutes / 60;
            return hours+ ":" + (minutes - hours * 60);
        }
        catch(NumberFormatException e)
        {
            LoggerFactory.getLogger(getClass()).error("Can't convert to date '"+duration+"'.", e);
            throw new IllegalArgumentException(e);
        }
    }

    @Override
    public String formatMoney(String amount, String currencyCode)
    {
        if (amount == null || amount.isEmpty())
        {
            return "";
        }
        try
        {
            float value = Float.valueOf(amount);
            if (value == 0)
            {
                return "-";
            }
            Currency currency = Currency.getInstance(currencyCode);
            final NumberFormat formatter = DecimalFormat.getInstance(bundle.getLocal());
            formatter.setCurrency(currency);
            formatter.setMinimumFractionDigits(0);
            formatter.setMaximumFractionDigits(currency.getDefaultFractionDigits());
            return formatter.format(value) + " " + currency.getSymbol(bundle.getLocal());
        }
        catch(NumberFormatException e)
        {
            LoggerFactory.getLogger(getClass()).error("Can't convert to float '"+amount+"'.", e);
            throw new IllegalArgumentException(e);
        }
    }

    @Override
    public String formatInteger(String integerString)
    {
        if (integerString == null || integerString.isEmpty())
        {
            return "";
        }
        try
        {
            int value = Integer.valueOf(integerString);
            return NumberFormat.getNumberInstance(bundle.getLocal()).format(value);
        }
        catch(NumberFormatException e)
        {
            LoggerFactory.getLogger(getClass()).error("Can't convert to integer '"+integerString+"'.", e);
            throw new IllegalArgumentException(e);
        }
    }

    @Override
    public String formatFloat(String numberString, String numberOfDigits)
    {
        if (numberString == null || numberString.isEmpty())
        {
            return "";
        }
        try
        {
            float value = Float.valueOf(numberString);
            int digits = Integer.valueOf(numberOfDigits);
            final NumberFormat formatter = DecimalFormat.getInstance(bundle.getLocal());
            formatter.setMaximumFractionDigits(digits);
            return formatter.format(value);
        }
        catch(NumberFormatException e)
        {
            LoggerFactory.getLogger(getClass()).error("Can't convert to float '"+numberString+"'.", e);
            throw new IllegalArgumentException(e);
        }
    }

    @Override
    public String formatPercentage(String numberString, String numberOfDigits)
    {
        if (numberString == null || numberString.isEmpty())
        {
            return "";
        }
        try
        {
            float value = Float.valueOf(numberString);
            if (value == 0)
            {
                return "-";
            }
            int digits = Integer.valueOf(numberOfDigits);
            final NumberFormat formatter = DecimalFormat.getPercentInstance(bundle.getLocal());
            formatter.setMaximumFractionDigits(digits);
            return formatter.format(value);
        }
        catch(NumberFormatException e)
        {
            LoggerFactory.getLogger(getClass()).error("Can't convert to float '"+numberString+"'.", e);
            throw new IllegalArgumentException(e);
        }
    }
}
