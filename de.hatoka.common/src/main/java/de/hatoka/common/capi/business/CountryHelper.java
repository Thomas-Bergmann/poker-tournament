package de.hatoka.common.capi.business;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.TimeZone;

public class CountryHelper
{
    public static final TimeZone UTC = TimeZone.getTimeZone("UTC");
    public static final TimeZone TZ_BERLIN= TimeZone.getTimeZone("Europe/Berlin");

    private final static Set<String> COUNTRIES = new HashSet<>();
    private final static Map<String, Locale> LOCALES = new HashMap<>();
    private final static Collection<String> TIMEZONE_IDS = new HashSet<>(Arrays.asList(TimeZone.getAvailableIDs()));
    private final static Collection<TimeZone> TIMEZONES;

    static
    {
        Collections.addAll(COUNTRIES, Locale.getISOCountries());
        for (Locale locale : Locale.getAvailableLocales())
        {
            LOCALES.put(locale.toString(), locale);
        }
        ArrayList<TimeZone> timeZones = new ArrayList<>();
        for (String timeZoneID : TimeZone.getAvailableIDs())
        {
            timeZones.add(TimeZone.getTimeZone(timeZoneID));
        }
        // fix that collection for directly return
        TIMEZONES = Collections.unmodifiableCollection(timeZones);
    }

    public CountryHelper()
    {
    }

    public boolean existsCountry(String countryID)
    {
        return COUNTRIES.contains(countryID);
    }

    public Locale getLocale(String localeID)
    {
        return LOCALES.get(localeID);
    }

    /**
     * @param timeZoneID
     *            An identifier of a time zone ({@link TimeZone#getID()})
     * @return <code>true</code> if given identifier is a valid time zone
     *         identifier
     */
    public boolean isValidTimeZoneID(String timeZoneID)
    {
        return TIMEZONE_IDS.contains(timeZoneID);
    }

    /**
     * @param timeZoneID
     *            An identifier of a time zone ({@link TimeZone#getID()})
     * @return The time zone with given identifier.
     * @throws IllegalArgumentException
     *             if the time zone is invalid.
     */
    public TimeZone getTimeZone(String timeZoneID)
    {
        if (!isValidTimeZoneID(timeZoneID))
        {
            throw new IllegalArgumentException("TimeZone not available: " + timeZoneID);
        }
        return TimeZone.getTimeZone(timeZoneID);
    }

    /**
     * @return All available time zones.
     */
    public Collection<TimeZone> getTimeZones()
    {
        return TIMEZONES;
    }
}
