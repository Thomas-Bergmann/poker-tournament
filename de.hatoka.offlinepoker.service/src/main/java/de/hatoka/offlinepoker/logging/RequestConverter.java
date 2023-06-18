package de.hatoka.offlinepoker.logging;

import jakarta.servlet.http.HttpServletRequest;

import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import ch.qos.logback.classic.pattern.ClassicConverter;
import ch.qos.logback.classic.spi.ILoggingEvent;

public class RequestConverter extends ClassicConverter
{
    @Override
    public String convert(ILoggingEvent event) {
        String requestInfo = "";
        try
        {
            requestInfo = extractRequestInfo(requestInfo);
        }
        catch(Exception e)
        {
            // exception not handled to avoid system failures because of logging
        }
        return requestInfo;
    }

    private String extractRequestInfo(String requestInfo)
    {
        RequestAttributes attrs = RequestContextHolder.getRequestAttributes();
        if (attrs != null && attrs instanceof ServletRequestAttributes) {
            HttpServletRequest request = ((ServletRequestAttributes)attrs).getRequest();
            requestInfo = "[" + request.getMethod() + ":'" + request.getRequestURI() + "']";
        }
        return requestInfo;
    }

}
