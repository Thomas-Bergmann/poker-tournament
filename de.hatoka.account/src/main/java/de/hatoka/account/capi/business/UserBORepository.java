package de.hatoka.account.capi.business;


public interface UserBORepository
{
    /**
     * Creates an user
     *
     * @param login
     * @return user
     */
    UserBO createUserBO(String login);

    /**
     * Retrieves a user via identifier
     *
     * @param userID
     * @return
     */
    UserBO getUserBOByID(String userID);

    /**
     * Retrieves a user via login
     * @param login
     * @return
     */
    UserBO getUserBOByLogin(String login);
}
