package de.hatoka.common.capi.business;

public interface BusinessObject
{
    /**
     *
     * @return the identifier (signature) of the business object
     */
    String getID();

    /**
     * Removes that business
     */
    void remove();

}
