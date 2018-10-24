package de.hatoka.common.capi.math;


/**
 * Represents unit of measurement. E.g. kilogram
 */
public enum Unit
{
    PCS(""), GRAM("g"), METER("m"), LITER("l");

    private final String abbreviation;

    /**
     * @param abbreviation
     */
    private Unit(String abbreviation)
    {
        this.abbreviation = abbreviation;
    }

    @Override
    public String toString()
    {
        return abbreviation;
    }
}
