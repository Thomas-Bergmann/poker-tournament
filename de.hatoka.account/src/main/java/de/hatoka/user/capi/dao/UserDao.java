package de.hatoka.user.capi.dao;

import de.hatoka.common.capi.dao.Dao;
import de.hatoka.user.capi.entities.UserPO;

public interface UserDao extends Dao<UserPO>
{
    /**
     *
     * @param login
     * @return
     */
    public UserPO createAndInsert(String login);

    /**
     *
     * @param login
     * @return user with the given login
     */
    public UserPO getByLogin(String login);

}