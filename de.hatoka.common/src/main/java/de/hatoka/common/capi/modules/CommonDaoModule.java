package de.hatoka.common.capi.modules;

import com.google.inject.Binder;
import com.google.inject.Module;

import de.hatoka.common.capi.dao.EncryptionUtils;
import de.hatoka.common.capi.dao.SequenceProvider;
import de.hatoka.common.capi.dao.UUIDGenerator;
import de.hatoka.common.internal.dao.EncryptionUtilsImpl;
import de.hatoka.common.internal.dao.SequenceProviderImpl;
import de.hatoka.common.internal.dao.UUIDGeneratorImpl;

public class CommonDaoModule implements Module
{
    private long start;

    public CommonDaoModule()
    {
        this(0);
    }

    public CommonDaoModule(long sequenceStartPosition)
    {
        start = sequenceStartPosition;
    }

    @Override
    public void configure(Binder binder)
    {
        binder.bind(UUIDGenerator.class).to(UUIDGeneratorImpl.class).asEagerSingleton();
        binder.bind(EncryptionUtils.class).to(EncryptionUtilsImpl.class).asEagerSingleton();
        binder.bind(SequenceProvider.class).toInstance(new SequenceProviderImpl(start));
    }

}
