package de.hatoka.common.capi.app.xslt;

import javax.ws.rs.core.UriInfo;

import de.hatoka.common.capi.app.servlet.ResourceService;

/**
 * static xslt library for accessing localizer methods
 *
 */
public class Lib
{
    public static final String XSLT_LOCALIZER = "localizer";

    public static String formatPercentage(Localizer localizer, String floatString)
    {
        return localizer.formatPercentage(floatString);
    }

    public static String formatInteger(Localizer localizer, String integerString)
    {
        return localizer.formatInteger(integerString);
    }

    public static String formatMoney(Localizer localizer, String amountString, String currencyCode)
    {
        return localizer.formatMoney(amountString, currencyCode);
    }

    public static String formatDate(Localizer localizer, String dateString)
    {
        return localizer.formatDate(dateString);
    }

    public static String formatDateTime(Localizer localizer, String dateString)
    {
        return localizer.formatDateTime(dateString);
    }

    public static String formatTime(Localizer localizer, String dateString)
    {
        return localizer.formatTime(dateString);
    }

    public static String formatDuration(Localizer localizer, String duration)
    {
        return localizer.formatDuration(duration);
    }

    public static String getResourceURI(String info, String resourceName)
    {
        return "../resources/" + resourceName;
    }

    public static String getResourceURI(UriInfo info, String resourceName)
    {
        return info.getBaseUriBuilder().path(ResourceService.class, "getResource").buildFromEncoded("resources/" + resourceName).toString();
    }

    public static String getText(Localizer localizer, String key)
    {
        return localizer.getText(key, "{" + key + "}");
    }

    public static String getText(Localizer localizer, String key, String defaultText)
    {
        return localizer.getText(key, defaultText);
    }

    public static String getText(Localizer localizer, String key, String defaultText, String firstParameter)
    {
        return localizer.getText(key, defaultText, firstParameter);
    }
    public static String getText(Localizer localizer, String key, String defaultText, String firstParameter, String secondParameter)
    {
        return localizer.getText(key, defaultText, firstParameter, secondParameter);
    }
    public static String getText(Localizer localizer, String key, String defaultText, String firstParameter, String secondParameter, String thirdParameter)
    {
        return localizer.getText(key, defaultText, firstParameter,secondParameter, thirdParameter);
    }
}
