package de.hatoka.user.capi.business;

import java.util.Locale;
import java.util.TimeZone;

public interface UserBO
{
    /**
     * @return the identifier of the user
     */
    UserRef getRef();

    /**
     * @return default nickname for players
     */
    String getNickName();

    /**
     * set default nickname for user
     *
     * @param nickName
     */
    void setNickName(String nickName);

    /**
     * @return true if user can login
     */
    boolean isActive();

    /**
     * @return user preferred locale
     */
    Locale getLocale();

    /**
     * Set user preferred locale.
     *
     * @param locale
     */
    void setLocale(Locale locale);

    /**
     * @return user preferred time zone
     */
    TimeZone getTimeZone();

    /**
     * Set user preferred time zone.
     *
     * @param timezone
     */
    void setTimeZone(TimeZone timezone);

    /**
     * Removes this user
     */
    void remove();
}
