package de.hatoka.address.capi.business;

import java.util.Collection;

public interface AddressBORepository extends AddressBOLifecycleListener
{
    /**
     * Removes all addresses from repository
     */
    void clean();

    /**
     *
     * @param addressBO
     * @return true if repository contains the address
     */
    boolean contains(AddressBO addressBO);

    /**
     * Creates an address
     *
     * @return address
     */
    AddressBO createAddressBO();

    /**
     * Retrieves all addresses of this repository
     * @return addresses
     */
    Collection<AddressBO> getAll();

    /**
     * Retrieves the address by identifier
     * @param addressRef
     * @return
     */
    AddressBO getByID(String addressRef);
}
