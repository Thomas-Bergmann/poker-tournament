package de.hatoka.user.capi.business;


public interface UserBORepository
{
    /**
     * Creates an user
     *
     * @param externalRef (identifier declared by authentication provider)
     * @return user
     */
    UserBO createUser(String externalRef);

    /**
     * Retrieves a user via identifier
     *
     * @param externalRef (identifier declared by authentication provider)
     * @return
     */
    UserBO getUser(String externalRef);
}
