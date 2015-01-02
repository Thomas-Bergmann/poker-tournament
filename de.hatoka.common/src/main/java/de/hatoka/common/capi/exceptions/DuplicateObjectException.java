package de.hatoka.common.capi.exceptions;

public class DuplicateObjectException extends IllegalArgumentException
{
    private static final long serialVersionUID = 1L;

    public DuplicateObjectException(String message)
    {
        super(message);
    }
}
