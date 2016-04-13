package de.hatoka.user.internal.config;

import java.util.Properties;

import de.hatoka.user.capi.config.UserConfiguration;

/**
 * configurable via system environment use -DACCOUNT_FROM_EMAIL=mail@test-mail.de
 */
public class UserConfigurationSystemEnvImpl implements UserConfiguration
{
    private static final String ACCOUNT_FROM_EMAIL = "ACCOUNT_FROM_EMAIL";
    private static final String ACCOUNT_SECRET = "ACCOUNT_SECRET";

    @Override
    public String getFromAddressForAccountRegistration()
    {
        Properties prop = System.getProperties();
        return prop.getProperty(ACCOUNT_FROM_EMAIL);
    }

    @Override
    public String getSecret()
    {
        Properties prop = System.getProperties();
        return prop.getProperty(ACCOUNT_SECRET);
    }
}
