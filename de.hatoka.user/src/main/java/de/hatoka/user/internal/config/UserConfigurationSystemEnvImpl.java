package de.hatoka.user.internal.config;

import javax.inject.Inject;

import de.hatoka.common.capi.configuration.SystemPropertyProvider;
import de.hatoka.user.capi.config.UserConfiguration;

/**
 * configurable via system environment use -DACCOUNT_FROM_EMAIL=mail@test-mail.de
 */
public class UserConfigurationSystemEnvImpl implements UserConfiguration
{
    private static final String ACCOUNT_FROM_EMAIL = "ACCOUNT_FROM_EMAIL";
    private static final String ACCOUNT_SECRET = "ACCOUNT_SECRET";

    @Inject
    private SystemPropertyProvider system;

    @Override
    public String getFromAddressForAccountRegistration()
    {
        return system.get(ACCOUNT_FROM_EMAIL);
    }

    @Override
    public String getSecret()
    {
        return system.get(ACCOUNT_SECRET);
    }
}
