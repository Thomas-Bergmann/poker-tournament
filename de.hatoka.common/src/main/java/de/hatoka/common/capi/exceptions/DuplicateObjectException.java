package de.hatoka.common.capi.exceptions;

public class DuplicateObjectException extends RuntimeException
{
    private static final long serialVersionUID = 1L;

    public DuplicateObjectException(String type, String key, String value)
    {
        super("Object of type: '" + type + "' has contains violation " + key + " with data=" + value);
    }
}
