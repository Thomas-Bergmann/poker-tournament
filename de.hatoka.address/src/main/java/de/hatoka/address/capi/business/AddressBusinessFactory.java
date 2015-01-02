package de.hatoka.address.capi.business;

import de.hatoka.address.capi.doa.AddressDao;
import de.hatoka.address.capi.entities.AddressPO;

public interface AddressBusinessFactory
{
    public AddressBO createAddressBO(AddressPO addressPO, AddressBOLifecycleListener addressLifecycleListener,
                    AddressDao addressDao);
}