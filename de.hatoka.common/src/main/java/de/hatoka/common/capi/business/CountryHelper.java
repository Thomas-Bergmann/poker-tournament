package de.hatoka.common.capi.business;

import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

public class CountryHelper
{
    private static Set<String> COUNTRIES = new HashSet<>();
    private static Map<String, Locale> LOCALES = new HashMap<>();

    static
    {
        Collections.addAll(COUNTRIES, Locale.getISOCountries());
        for(Locale locale : Locale.getAvailableLocales())
        {
            LOCALES.put(locale.toString(), locale);
        }
    }

    public CountryHelper()
    {
    }

    public boolean exists(String countryID)
    {
        return COUNTRIES.contains(countryID);
    }

    public Locale get(String localeID)
    {
        return LOCALES.get(localeID);
    }
}
