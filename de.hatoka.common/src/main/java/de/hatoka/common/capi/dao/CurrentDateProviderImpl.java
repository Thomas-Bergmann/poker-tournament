package de.hatoka.common.capi.dao;

import java.util.Date;

import com.google.inject.Provider;

public class CurrentDateProviderImpl implements Provider<Date>
{
    @Override
    public Date get()
    {
        return new Date();
    }

}
