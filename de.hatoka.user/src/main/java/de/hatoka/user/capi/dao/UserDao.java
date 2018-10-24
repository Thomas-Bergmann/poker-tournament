package de.hatoka.user.capi.dao;

import de.hatoka.common.capi.dao.Dao;
import de.hatoka.user.capi.entities.UserPO;

public interface UserDao extends Dao<UserPO>
{
    /**
     *
     * @param identifier
     * @return
     */
    public UserPO createAndInsert(String externalRef);

    /**
     *
     * @param externalRef
     * @return user with the given login
     */
    public UserPO getByExternalRef(String externalRef);

}