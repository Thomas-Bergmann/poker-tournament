package de.hatoka.common.capi.rest;

public class JsonSerializationException extends RuntimeException
{
    /**
     * serialVersionUID
     */
    private static final long serialVersionUID = -2645273110169623826L;

    public JsonSerializationException(Exception e)
    {
        super("Error while serializing", e);
    }


    public JsonSerializationException(String msg, Exception e)
    {
        super(msg, e);
    }
}
