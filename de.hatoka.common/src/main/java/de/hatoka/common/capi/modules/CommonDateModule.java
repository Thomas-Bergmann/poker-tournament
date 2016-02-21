package de.hatoka.common.capi.modules;

import java.util.Date;

import com.google.inject.Binder;
import com.google.inject.Module;

import de.hatoka.common.capi.dao.CurrentDateProviderImpl;

public class CommonDateModule implements Module
{
    @Override
    public void configure(Binder binder)
    {
        binder.bind(Date.class).toProvider(CurrentDateProviderImpl.class);
    }

}
