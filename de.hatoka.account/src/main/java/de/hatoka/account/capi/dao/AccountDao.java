package de.hatoka.account.capi.dao;

import de.hatoka.account.capi.entities.AccountPO;
import de.hatoka.account.capi.entities.UserPO;
import de.hatoka.common.capi.dao.Dao;

public interface AccountDao extends Dao<AccountPO>
{
    /**
     *
     * @param email
     * @return
     */
    AccountPO createAndInsert(UserPO owner);
}
