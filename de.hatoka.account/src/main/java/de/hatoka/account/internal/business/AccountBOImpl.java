package de.hatoka.account.internal.business;

import de.hatoka.account.capi.business.AccountBO;
import de.hatoka.account.capi.business.AccountBusinessFactory;
import de.hatoka.account.capi.business.UserBO;
import de.hatoka.account.capi.dao.AccountDao;
import de.hatoka.account.capi.entities.AccountPO;

public class AccountBOImpl implements AccountBO
{
    private final AccountBusinessFactory businessFactory;
    private final AccountDao accountDao;

    private AccountPO accountPO;

    public AccountBOImpl(AccountPO accountPO, AccountDao accountDao, AccountBusinessFactory businessFactory)
    {
        this.accountPO = accountPO;
        this.accountDao = accountDao;
        this.businessFactory = businessFactory;
    }

    @Override
    public boolean equals(Object obj)
    {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        AccountBOImpl other = (AccountBOImpl)obj;
        if (accountPO == null)
        {
            if (other.accountPO != null)
                return false;
        }
        else if (!accountPO.equals(other.accountPO))
            return false;
        return true;
    }

    @Override
    public String getID()
    {
        return accountPO.getId();
    }

    @Override
    public String getName()
    {
        return accountPO.getName();
    }

    @Override
    public UserBO getOwner()
    {
        return businessFactory.getUserBO(accountPO.getOwner());
    }

    @Override
    public int hashCode()
    {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((accountPO == null) ? 0 : accountPO.hashCode());
        return result;
    }

    @Override
    public boolean isActive()
    {
        return accountPO.isActive();
    }

    @Override
    public void remove()
    {
        accountDao.remove(accountPO);
        accountPO = null;
    }

    @Override
    public void setName(String name)
    {
        accountPO.setName(name);
    }

}
