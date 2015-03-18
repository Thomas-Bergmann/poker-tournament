package de.hatoka.common.capi.app.xslt;

/**
 * Provides a localized text, with java message parameter injection.
 */
public interface Localizer
{
    /**
     * Format the date
     * @param date
     * @return
     */
    String formatDate(String dateString);

    /**
     * Format the date to date and time
     * @param date
     * @return
     */
    String formatDateTime(String dateString);

    /**
     * Localize text
     *
     * @param key
     * @param defaultText
     * @param objects
     * @return localized text (null if defaultText is null and key could not be resolved)
     */
    String getText(String key, String defaultText, Object... objects);
}
