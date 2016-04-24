package de.hatoka.common.internal.configuration;

import de.hatoka.common.capi.configuration.SystemPropertyProvider;

public class SystemPropertyProviderImpl implements SystemPropertyProvider
{
    @Override
    public String get(String key)
    {
        return System.getProperty(key, System.getenv(key));
    }
}
