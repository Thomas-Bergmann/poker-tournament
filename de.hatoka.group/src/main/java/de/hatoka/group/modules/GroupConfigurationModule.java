package de.hatoka.group.modules;

import com.google.inject.Binder;
import com.google.inject.Module;

import de.hatoka.group.capi.config.GroupConfiguration;
import de.hatoka.group.internal.config.GroupConfigurationSystemEnvImpl;

public class GroupConfigurationModule implements Module
{

    @Override
    public void configure(Binder binder)
    {
        binder.bind(GroupConfiguration.class).to(GroupConfigurationSystemEnvImpl.class).asEagerSingleton();
    }
}
