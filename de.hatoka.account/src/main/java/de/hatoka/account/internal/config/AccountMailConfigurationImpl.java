package de.hatoka.account.internal.config;

import de.hatoka.account.capi.config.AccountMailConfiguration;

/**
 * Straight forward implementation
 * TODO must be configurable for deployment
 */
public class AccountMailConfigurationImpl implements AccountMailConfiguration
{
    @Override
    public String getFromAddressForAccountRegistration()
    {
        return "offlinepoker@hatoka.da";
    }
}
