package de.hatoka.account.internal.business;

import java.util.List;
import java.util.stream.Collectors;

import de.hatoka.account.capi.business.AccountBO;
import de.hatoka.account.capi.business.AccountBORepository;
import de.hatoka.account.capi.business.AccountBusinessFactory;
import de.hatoka.account.capi.dao.AccountDao;
import de.hatoka.account.capi.entities.AccountPO;
import de.hatoka.account.capi.entities.UserPO;

public class AccountBORepositoryImpl implements AccountBORepository
{
    private final AccountBusinessFactory businessFactory;
    private final AccountDao accountDao;
    private final UserPO owner;

    public AccountBORepositoryImpl(AccountDao accountDao, UserPO owner, AccountBusinessFactory businessFactory)
    {
        this.accountDao = accountDao;
        this.owner = owner;
        this.businessFactory = businessFactory;
    }

    @Override
    public AccountBO createAccountBO()
    {
        AccountPO accountPO = accountDao.createAndInsert(owner);
        accountPO.setActive(true);
        return getBO(accountPO);
    }

    @Override
    public AccountBO getAccountBOById(String id)
    {
        AccountPO accountPO = accountDao.getById(id);
        if (accountPO == null)
        {
            return null;
        }
        if (!accountPO.getOwner().equals(owner))
        {
            return null;
        }
        return getBO(accountPO);
    }

    @Override
    public List<AccountBO> getAccountBOs()
    {
        return owner.getAccountPOs().stream().map(accountPO -> getBO(accountPO)).collect(Collectors.toList());
    }

    private AccountBO getBO(AccountPO accountPO)
    {
        return businessFactory.getAccountBO(accountPO);
    }

}
