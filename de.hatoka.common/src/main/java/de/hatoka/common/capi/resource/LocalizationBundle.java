package de.hatoka.common.capi.resource;

import java.util.HashSet;
import java.util.Locale;
import java.util.MissingResourceException;
import java.util.PropertyResourceBundle;
import java.util.ResourceBundle;
import java.util.Set;
import java.util.TimeZone;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.hatoka.common.capi.business.CountryHelper;

public class LocalizationBundle
{
    private static final Logger LOGGER = LoggerFactory.getLogger(LocalizationBundle.class);

    private final ResourceBundle resourceBundle;
    private final Locale locale;
    private final String resourceBundleName;
    private final Set<String> unkownKeys = new HashSet<>();
    private final TimeZone timezone;

    @Deprecated
    public LocalizationBundle(String resourceBundleName, Locale locale)
    {
        this(resourceBundleName, locale, CountryHelper.UTC);
    }

    public LocalizationBundle(String resourceBundleName, Locale locale, TimeZone timeZone)
    {
        resourceBundle = PropertyResourceBundle.getBundle(resourceBundleName, locale);
        this.resourceBundleName = resourceBundleName;
        this.locale = locale;
        this.timezone = timeZone;
    }

    public Locale getLocal()
    {
        return locale;
    }

    public TimeZone getTimeZone()
    {
        return timezone;
    }

    public String getText(String key)
    {
        return getText(key, "{" + key + "}");
    }

    public String getText(String key, String defaultText)
    {
        if (unkownKeys.contains(key))
        {
            return defaultText;
        }
        try
        {
            return resourceBundle.getString(key);
        }
        catch(MissingResourceException ex)
        {
            if (!unkownKeys.contains(key))
            {
                LOGGER.warn("Can't resolve key '" + key + "' inside of resource '" + resourceBundleName
                                + "' and locale '" + locale + "'.", ex);
                unkownKeys.add(key);
            }
        }
        return defaultText;
    }

}
