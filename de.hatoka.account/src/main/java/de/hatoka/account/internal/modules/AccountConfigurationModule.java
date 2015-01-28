package de.hatoka.account.internal.modules;

import com.google.inject.Binder;
import com.google.inject.Module;

import de.hatoka.account.capi.config.AccountConfiguration;
import de.hatoka.account.internal.config.AccountConfigurationSystemEnvImpl;

public class AccountConfigurationModule implements Module
{
    @Override
    public void configure(Binder binder)
    {
        binder.bind(AccountConfiguration.class).to(AccountConfigurationSystemEnvImpl.class).asEagerSingleton();
    }
}