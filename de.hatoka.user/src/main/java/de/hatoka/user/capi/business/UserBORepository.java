package de.hatoka.user.capi.business;

import java.util.List;
import java.util.Optional;

public interface UserBORepository
{
    /**
     * Creates an user
     *
     * @param externalRef
     *            (identifier declared by authentication provider)
     * @return user
     */
    UserBO createUser(UserRef externalRef);

    /**
     * Retrieves a user via identifier
     *
     * @param externalRef
     *            (identifier declared by authentication provider)
     * @return
     */
    Optional<UserBO> findUser(UserRef externalRef);

    List<UserBO> getAllUsers();

    /**
     * Removes all users of this repository
     */
    default void clear()
    {
        getAllUsers().forEach(UserBO::remove);
    }
}
