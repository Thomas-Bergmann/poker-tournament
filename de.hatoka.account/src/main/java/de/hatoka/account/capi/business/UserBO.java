package de.hatoka.account.capi.business;

import java.net.URI;
import java.util.Locale;

public interface UserBO
{
    /**
     * @return the identifier (artificial key)
     */
    String getID();

    /**
     * Removes that object
     */
    void remove();

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
     * @return email of user
     */
    String getEmail();

    /**
     * @return name for salutation
     */
    String getNickName();

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
     * Verifies the given password
     *
     * @param password
     * @return false if user is inactive or password doesn't match
     */
    boolean verifyPassword(String password);
}
