package de.hatoka.account.capi.config;

/**
 * Account Mail Configuration
 */
public interface AccountMailConfiguration
{
    /**
     * @return mail address, which is used as from address for account verification
     */
    String getFromAddressForAccountRegistration();
}
