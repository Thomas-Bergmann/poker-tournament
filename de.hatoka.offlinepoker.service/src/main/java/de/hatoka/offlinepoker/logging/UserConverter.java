package de.hatoka.offlinepoker.logging;

import ch.qos.logback.classic.pattern.ClassicConverter;
import ch.qos.logback.classic.spi.ILoggingEvent;

public class UserConverter extends ClassicConverter
{

    @Override
    public String convert(ILoggingEvent event)
    {
        String userInformation = "";
        try
        {
            userInformation = extractUserInformation(userInformation);
        }
        catch(Exception e)
        {
            // exception not handled to avoid system failures because of logging
        }
        return userInformation;
    }

    private String extractUserInformation(String userInformation)
    {
        // userInformation = "[" + auth.getName() + " " + userEmail + "]";
        return userInformation;
    }

}
