package de.hatoka.common.capi.modules;

import com.google.inject.Binder;
import com.google.inject.Module;

import de.hatoka.common.capi.dao.UUIDGenerator;
import de.hatoka.common.capi.dao.UUIDGeneratorImpl;


public class CommonDaoModule implements Module
{
    @Override
    public void configure(Binder binder)
    {
        binder.bind(UUIDGenerator.class).to(UUIDGeneratorImpl.class).asEagerSingleton();
    }

}
