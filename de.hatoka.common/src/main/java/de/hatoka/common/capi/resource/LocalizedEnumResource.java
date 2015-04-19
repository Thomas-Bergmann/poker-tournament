package de.hatoka.common.capi.resource;

import java.util.Locale;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import de.hatoka.common.capi.business.CountryHelper;

/**
 * LocalizedEnumResource provides easy access to name and description of an enum
 */
public class LocalizedEnumResource
{
    private final String prefix;
    private final String location;
    private final Map<Locale, LocalizationBundle> bundles = new ConcurrentHashMap<>();

    public LocalizedEnumResource(String prefix, String location)
    {
        this.location = location;
        this.prefix = prefix;
    }

    private synchronized LocalizationBundle getBundle(Locale locale)
    {
        LocalizationBundle result = bundles.get(locale);
        if (result == null)
        {
            result = new LocalizationBundle(location, locale, CountryHelper.UTC);
            bundles.put(locale, result);
        }
        return result;
    }

    public String getDescription(Enum<?> aEnum, Locale locale)
    {
        LocalizationBundle bundle = getBundle(locale);
        return bundle.getText(prefix + aEnum.name() + ".descr");
    }

    public String getName(Enum<?> aEnum, Locale locale)
    {
        LocalizationBundle bundle = getBundle(locale);
        return bundle.getText(prefix + aEnum.name()+ ".name");
    }
}
