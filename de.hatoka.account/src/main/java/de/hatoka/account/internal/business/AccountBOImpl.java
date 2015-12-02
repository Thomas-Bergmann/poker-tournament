package de.hatoka.account.internal.business;

import de.hatoka.account.capi.business.AccountBO;
import de.hatoka.account.capi.business.AccountBusinessFactory;
import de.hatoka.account.capi.business.UserBO;
import de.hatoka.account.capi.dao.AccountDao;
import de.hatoka.account.capi.entities.AccountPO;
import de.hatoka.address.capi.business.AddressBO;
import de.hatoka.address.capi.business.AddressBOLifecycleListener;
import de.hatoka.address.capi.doa.AddressDao;
import de.hatoka.address.capi.entities.AddressPO;

public class AccountBOImpl implements AccountBO, AddressBOLifecycleListener
{
    private final AccountBusinessFactory businessFactory;
    private final AccountDao accountDao;
    private final AddressDao addressDao;

    private AccountPO accountPO;

    public AccountBOImpl(AccountPO accountPO, AccountDao accountDao, AccountBusinessFactory businessFactory,
                    AddressDao addressDao)
    {
        this.accountPO = accountPO;
        this.accountDao = accountDao;
        this.businessFactory = businessFactory;
        this.addressDao = addressDao;
    }

    @Override
    public void cloned(AddressBO addressBO, String oldAddressRef)
    {
        if (accountPO.getAddressRef() != null && oldAddressRef.equals(accountPO.getAddressRef()))
        {
            accountPO.setAddressRef(addressBO.getID());
        }
    }

    @Override
    public AddressBO createAddressBO()
    {
        if (accountPO.getAddressRef() != null)
        {
            throw new IllegalStateException("Address of account exist.");
        }
        AddressPO addressPO = addressDao.createAndInsert(accountPO.getId());
        accountPO.setAddressRef(addressPO.getId());
        return businessFactory.getAddressBO(addressPO, this);
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
    public AddressBO getAddressBO()
    {
        String addressRef = accountPO.getAddressRef();
        if (addressRef == null)
        {
            return null;
        }
        AddressPO addressPO = addressDao.getById(addressRef);
        return businessFactory.getAddressBO(addressPO, this);
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
    public void removedAddressRef(String oldAddressRef)
    {
        if (oldAddressRef.equals(accountPO.getAddressRef()))
        {
            accountPO.setAddressRef(null);
        }
    }

    @Override
    public void setName(String name)
    {
        accountPO.setName(name);
    }

}
