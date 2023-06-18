package de.hatoka.common.capi.rest;

public class DateParseException extends RuntimeException
{
    /**
     * serialVersionUID
     */
    private static final long serialVersionUID = -2645273110169623826L;

    public DateParseException(Exception e, String elementName)
    {
        super("Error while parsing element " + elementName, e);
    }


    public DateParseException(String msg, Exception e)
    {
        super(msg, e);
    }
}
