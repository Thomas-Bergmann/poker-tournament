package de.hatoka.account.internal.modules;

import com.google.inject.Binder;
import com.google.inject.Module;

import de.hatoka.account.capi.business.UserBusinessFactory;
import de.hatoka.account.internal.business.UserBusinessFactoryImpl;

public class AccountBusinessModule implements Module
{

    @Override
    public void configure(Binder binder)
    {
        binder.bind(UserBusinessFactory.class).to(UserBusinessFactoryImpl.class).asEagerSingleton();
    }
}
