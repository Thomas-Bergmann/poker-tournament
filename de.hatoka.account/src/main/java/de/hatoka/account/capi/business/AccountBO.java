package de.hatoka.account.capi.business;

import de.hatoka.address.capi.business.AddressBO;
import de.hatoka.common.capi.business.BusinessObject;

/**
 * An account represents a billable customer of the system.
 */
public interface AccountBO extends BusinessObject
{
    /**
     * Creates an address for the account
     *
     * @return
     */
    AddressBO createAddressBO();

    /**
     * Get the address of account
     *
     * @return
     */
    AddressBO getAddressBO();

    /**
     * @return name of account (there is no need to make it unique)
     */
    String getName();

    /**
     * @return owner of account
     */
    UserBO getOwner();

    /**
     * @return true if account is active and can be used
     */
    boolean isActive();

    /**
     * Changes the name of the account
     * @param name
     */
    void setName(String name);
}
