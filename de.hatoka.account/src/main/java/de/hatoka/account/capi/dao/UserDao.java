package de.hatoka.account.capi.dao;

import de.hatoka.account.capi.entities.UserPO;
import de.hatoka.common.capi.dao.Dao;

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