package de.hatoka.common.capi.exceptions;

public class IllegalConfigurationException extends RuntimeException
{
    private static final long serialVersionUID = 1L;

    public IllegalConfigurationException(String message)
    {
        super(message);
    }

    public IllegalConfigurationException(String message, Throwable cause)
    {
        super(message, cause);
    }
}
