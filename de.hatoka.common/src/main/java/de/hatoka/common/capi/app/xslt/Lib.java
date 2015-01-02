package de.hatoka.common.capi.app.xslt;

import javax.ws.rs.core.UriInfo;

import de.hatoka.common.capi.app.servlet.ResourceService;

/**
 * static xslt library for accessing localizer methods
 *
 */
public class Lib
{
    public static String formatDate(Localizer localizer, String dateString)
    {
        return localizer.formatDate(dateString);
    }

    public static String getResourceURI(String info, String resourceName)
    {
        return "../resources/" + resourceName;
    }

    public static String getResourceURI(UriInfo info, String resourceName)
    {
        return info.getBaseUriBuilder().path(ResourceService.class, "getResource").buildFromEncoded("resources/" + resourceName).toString();
    }

    public static String getText(Localizer localizer, String key, String defaultText)
    {
        return localizer.getText(key, defaultText);
    }

    public static String getText(Localizer localizer, String key, String defaultText, String firstParameter)
    {
        return localizer.getText(key, defaultText, firstParameter);
    }
}
