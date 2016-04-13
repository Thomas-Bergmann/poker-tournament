package de.hatoka.user.internal.modules;

import com.google.inject.Binder;
import com.google.inject.Module;

import de.hatoka.user.capi.config.UserConfiguration;
import de.hatoka.user.internal.config.UserConfigurationSystemEnvImpl;

public class UserConfigurationModule implements Module
{
    @Override
    public void configure(Binder binder)
    {
        binder.bind(UserConfiguration.class).to(UserConfigurationSystemEnvImpl.class).asEagerSingleton();
    }
}