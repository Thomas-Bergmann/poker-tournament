package de.hatoka.account.internal.modules;

import com.google.inject.Binder;
import com.google.inject.Module;

import de.hatoka.account.capi.config.AccountMailConfiguration;
import de.hatoka.account.internal.config.AccountMailConfigurationImpl;

public class AccountConfigurationModule implements Module
{
    @Override
    public void configure(Binder binder)
    {
        binder.bind(AccountMailConfiguration.class).to(AccountMailConfigurationImpl.class).asEagerSingleton();
    }
}