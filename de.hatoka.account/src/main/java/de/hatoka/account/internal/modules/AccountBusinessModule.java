package de.hatoka.account.internal.modules;

import com.google.inject.Binder;
import com.google.inject.Module;

import de.hatoka.account.capi.business.AccountBusinessFactory;
import de.hatoka.account.internal.business.AccountBusinessFactoryImpl;

public class AccountBusinessModule implements Module
{

    @Override
    public void configure(Binder binder)
    {
        binder.bind(AccountBusinessFactory.class).to(AccountBusinessFactoryImpl.class).asEagerSingleton();
    }
}
