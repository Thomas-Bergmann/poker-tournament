package de.hatoka.address.internal.modules;

import com.google.inject.Binder;
import com.google.inject.Module;

import de.hatoka.address.capi.business.AddressBusinessFactory;
import de.hatoka.address.internal.business.AddressBusinessFactoryImpl;

public class AddressBusinessModule implements Module
{

    @Override
    public void configure(Binder binder)
    {
        binder.bind(AddressBusinessFactory.class).to(AddressBusinessFactoryImpl.class).asEagerSingleton();
    }

}
