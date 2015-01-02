package de.hatoka.account.capi.business;

import java.net.URI;
import java.util.Locale;

import de.hatoka.address.capi.business.AddressBO;
import de.hatoka.address.capi.business.AddressBORepository;
import de.hatoka.common.capi.business.BusinessObject;

public interface UserBO extends BusinessObject
{
    /**
     * Activates the account, if the token matches.
     *
     * @param token
     * @return true if user can login
     */
    boolean applySignInToken(String token);

    /**
     * @return accounts of owner
     */
    AccountBORepository getAccountBORepository();

    /**
     * @return address repository of user, so addresses can be created, removed
     */
    AddressBORepository getAddressBORepository();

    /**
     * Retrieves the business address of the user.
     *
     * @return business address of user (null if there is no business address)
     */
    AddressBO getBusinessAddressBO();

    /**
     * @return email of user
     */
    String getEmail();

    /**
     * @return name for salutation
     */
    String getNickName();

    /**
     * Retrieves the address of the user, if there is no address, an empty
     * address will be created.
     *
     * @return private address of user
     */
    AddressBO getPrivateAddressBO();

    /**
     * @return true if user can login
     */
    boolean isActive();

    /**
     * Initialize registration (precondition login of user is null)
     *
     * @param login
     * @param email
     * @param password
     */
    void register(String email, String password);

    /**
     * Send email to the user, for email validation
     *
     * @param uri
     *            clicking this link enables the account
     */
    void sendEmailVerificationMail(URI uri);

    /**
     * Sets the business address of the user, the address must be an element of
     * the users address repository.
     */
    void setBusinessAddressBO(AddressBO addressBO);

    /**
     * Set user preferred locale.
     *
     * @param locale
     */
    void setLocale(Locale locale);

    /**
     * Sets name for salutation
     *
     * @param nickName
     */
    void setNickName(String nickName);

    /**
     * Sets the private address of the user, the address must be an element of
     * the users address repository.
     */
    void setPrivateAddressBO(AddressBO addressBO);

    /**
     * Verifies the given password
     *
     * @param password
     * @return false if user is inactive or password doesn't match
     */
    boolean verifyPassword(String password);
}
