package de.hatoka.common.capi.modules;

import com.google.inject.Binder;
import com.google.inject.Module;

import de.hatoka.common.capi.dao.EncryptionUtils;
import de.hatoka.common.capi.dao.UUIDGenerator;
import de.hatoka.common.internal.dao.EncryptionUtilsImpl;
import de.hatoka.common.internal.dao.UUIDGeneratorImpl;


public class CommonDaoModule implements Module
{
    @Override
    public void configure(Binder binder)
    {
        binder.bind(UUIDGenerator.class).to(UUIDGeneratorImpl.class).asEagerSingleton();
        binder.bind(EncryptionUtils.class).to(EncryptionUtilsImpl.class).asEagerSingleton();
    }

}
