package de.hatoka.address.internal.business;

import de.hatoka.address.capi.business.AddressBO;
import de.hatoka.address.capi.business.AddressBOLifecycleListener;
import de.hatoka.address.capi.business.AddressBusinessFactory;
import de.hatoka.address.capi.doa.AddressDao;
import de.hatoka.address.capi.entities.AddressPO;

public class AddressBusinessFactoryImpl implements AddressBusinessFactory
{
    public AddressBO createAddressBO(AddressPO addressPO, AddressBOLifecycleListener addressLifecycleListener, AddressDao addressDao)
    {
        return new AddressBOImpl(addressPO, addressLifecycleListener, addressDao);
    }
}