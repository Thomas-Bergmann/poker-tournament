package de.hatoka.common.capi.exceptions;

public class CopyNotSupportException extends RuntimeException
{
    private static final long serialVersionUID = 1L;

    public CopyNotSupportException(CloneNotSupportedException e)
    {
        super(e);
    }
}
