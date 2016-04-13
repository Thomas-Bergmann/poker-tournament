package de.hatoka.user.internal.modules;

import com.google.inject.Binder;
import com.google.inject.Module;

import de.hatoka.user.capi.business.UserBusinessFactory;
import de.hatoka.user.internal.business.UserBusinessFactoryImpl;

public class UserBusinessModule implements Module
{

    @Override
    public void configure(Binder binder)
    {
        binder.bind(UserBusinessFactory.class).to(UserBusinessFactoryImpl.class).asEagerSingleton();
    }
}
