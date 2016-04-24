package de.hatoka.user.internal.config;

import de.hatoka.user.capi.config.UserConfiguration;

public class UserTestConfiguration implements UserConfiguration
{

    @Override
    public String getFromAddressForAccountRegistration()
    {
        return "test@hatoka.de";
    }

    @Override
    public String getSecret()
    {
        return "secret";
    }

}
