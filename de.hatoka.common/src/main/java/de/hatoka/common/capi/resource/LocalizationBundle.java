package de.hatoka.common.capi.resource;

import java.util.HashSet;
import java.util.Locale;
import java.util.MissingResourceException;
import java.util.PropertyResourceBundle;
import java.util.ResourceBundle;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LocalizationBundle
{
    private static final Logger LOGGER = LoggerFactory.getLogger(LocalizationBundle.class);

    private final ResourceBundle resourceBundle;
    private final Locale locale;
    private final String resourceBundleName;
    private final Set<String> unkownKeys = new HashSet<>();

    public LocalizationBundle(String resourceBundleName, Locale locale)
    {
        resourceBundle = PropertyResourceBundle.getBundle(resourceBundleName, locale);
        this.resourceBundleName = resourceBundleName;
        this.locale = locale;
    }

    public Locale getLocal()
    {
        return locale;
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
