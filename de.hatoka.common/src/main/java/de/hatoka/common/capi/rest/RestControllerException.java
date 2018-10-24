package de.hatoka.common.capi.rest;

import org.springframework.http.HttpStatus;

public class RestControllerException extends RuntimeException
{

    private static final long serialVersionUID = -7724289129556554634L;

    private final HttpStatus httpStatus;
    private final String logMessage;

    public RestControllerException(HttpStatus httpStatus, String message, Throwable t)
    {
        super(message, t);
        this.httpStatus = httpStatus;
        this.logMessage = message;
    }

    public RestControllerException(HttpStatus httpStatus, String message)
    {
        super(message);
        this.httpStatus = httpStatus;
        this.logMessage = message;
    }

    public RestControllerException(HttpStatus httpStatus, String message, String logMessage)
    {
        super(message);
        this.httpStatus = httpStatus;
        this.logMessage = logMessage;
    }

    public HttpStatus getHttpStatus()
    {
        return httpStatus;
    }

    public String getLogMessage()
    {
        return logMessage;
    }
}
