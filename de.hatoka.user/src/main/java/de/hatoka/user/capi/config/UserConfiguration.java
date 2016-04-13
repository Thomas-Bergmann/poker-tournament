package de.hatoka.user.capi.config;

/**
 * Account Configuration
 */
public interface UserConfiguration
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
