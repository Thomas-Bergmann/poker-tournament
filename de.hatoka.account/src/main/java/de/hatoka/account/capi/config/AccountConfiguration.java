package de.hatoka.account.capi.config;

/**
 * Account Configuration
 */
public interface AccountConfiguration
{
    /**
     * @return mail address, which is used as from address for account verification
     */
    String getFromAddressForAccountRegistration();

    /**
     * @return signature secret
     */
    String getSecret();
}
