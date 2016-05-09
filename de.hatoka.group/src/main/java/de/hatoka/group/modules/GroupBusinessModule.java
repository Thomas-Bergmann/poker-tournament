package de.hatoka.group.modules;

import com.google.inject.Binder;
import com.google.inject.Module;

import de.hatoka.group.capi.business.GroupBusinessFactory;
import de.hatoka.group.internal.business.GroupBusinessFactoryImpl;

public class GroupBusinessModule implements Module
{
    @Override
    public void configure(Binder binder)
    {
        binder.bind(GroupBusinessFactory.class).to(GroupBusinessFactoryImpl.class).asEagerSingleton();
    }
}
