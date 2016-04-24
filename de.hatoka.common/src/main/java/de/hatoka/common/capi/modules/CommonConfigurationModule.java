package de.hatoka.common.capi.modules;

import com.google.inject.Binder;
import com.google.inject.Module;

import de.hatoka.common.capi.configuration.SystemPropertyProvider;
import de.hatoka.common.internal.configuration.SystemPropertyProviderImpl;

public class CommonConfigurationModule implements Module
{
    @Override
    public void configure(Binder binder)
    {
        binder.bind(SystemPropertyProvider.class).to(SystemPropertyProviderImpl.class).asEagerSingleton();
    }

}
