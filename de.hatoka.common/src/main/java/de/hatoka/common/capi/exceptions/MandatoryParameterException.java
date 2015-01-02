package de.hatoka.common.capi.exceptions;

public class MandatoryParameterException extends IllegalArgumentException
{
    private static final long serialVersionUID = 1L;

    public MandatoryParameterException(String parameterName)
    {
        super("Parameter '"+parameterName+"' is mandatory.");
    }
}
