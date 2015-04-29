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

    /**
     * Format the duration in to a time (eg. 90 to 01:30)
     * @param duration in minutes
     * @return time
     */
    String formatDuration(String duration);

    /**
     * Format (Extracts) the time of a date time object
     * @param date time
     * @return time
     */
    String formatTime(String dateString);
}
