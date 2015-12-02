package de.hatoka.address.internal.modules;

import com.google.inject.Binder;
import com.google.inject.Module;

import de.hatoka.address.capi.doa.AddressDao;
import de.hatoka.address.internal.dao.AddressDaoJpa;

public class AddressDaoModule implements Module
{
    @Override
    public void configure(Binder binder)
    {
        binder.bind(AddressDao.class).to(AddressDaoJpa.class).asEagerSingleton();
    }

}
