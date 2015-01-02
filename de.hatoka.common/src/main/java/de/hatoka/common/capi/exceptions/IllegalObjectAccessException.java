package de.hatoka.common.capi.exceptions;

public class IllegalObjectAccessException extends IllegalArgumentException
{
    private static final long serialVersionUID = 1L;

    public IllegalObjectAccessException(String message)
    {
        super(message);
    }
}
