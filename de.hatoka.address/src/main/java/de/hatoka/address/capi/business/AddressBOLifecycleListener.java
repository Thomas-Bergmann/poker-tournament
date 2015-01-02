package de.hatoka.address.capi.business;

public interface AddressBOLifecycleListener
{
    /**
     * Inform about changed reference.
     *
     * @param newAddressBO
     * @param oldAddressRef
     */
    void cloned(AddressBO newAddressBO, String oldAddressRef);

    /**
     * Inform about removed address
     * @param oldAddressRef
     */
    void removedAddressRef(String oldAddressRef);
}
