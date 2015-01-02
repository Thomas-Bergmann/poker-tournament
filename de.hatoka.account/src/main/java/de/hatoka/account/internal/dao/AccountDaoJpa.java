package de.hatoka.account.internal.dao;

import javax.inject.Inject;

import de.hatoka.account.capi.dao.AccountDao;
import de.hatoka.account.capi.entities.AccountPO;
import de.hatoka.account.capi.entities.UserPO;
import de.hatoka.common.capi.dao.GenericJPADao;
import de.hatoka.common.capi.dao.UUIDGenerator;

public class AccountDaoJpa extends GenericJPADao<AccountPO> implements AccountDao
{
    @Inject
    private UUIDGenerator uuidGenerator;

    public AccountDaoJpa()
    {
        super(AccountPO.class);
    }

    @Override
    public AccountPO createAndInsert(UserPO owner)
    {
        AccountPO result = create();
        result.setId(uuidGenerator.generate());
        result.setOwner(owner);
        insert(result);
        return result;
    }

    @Override
    public void remove(AccountPO accountPO)
    {
        accountPO.setOwner(null);
        super.remove(accountPO);
    }
}
