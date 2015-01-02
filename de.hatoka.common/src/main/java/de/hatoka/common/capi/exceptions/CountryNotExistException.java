package de.hatoka.common.capi.exceptions;

public class CountryNotExistException extends RuntimeException
{
    private static final long serialVersionUID = 1L;

    public CountryNotExistException(String nonExistingCountry)
    {
        super("Country '" + nonExistingCountry + "' doesn't exist.");
    }
}
