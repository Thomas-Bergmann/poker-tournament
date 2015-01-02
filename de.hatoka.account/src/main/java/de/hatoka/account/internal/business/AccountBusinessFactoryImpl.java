package de.hatoka.account.internal.business;

import javax.inject.Inject;

import com.google.inject.Injector;

import de.hatoka.account.capi.business.AccountBO;
import de.hatoka.account.capi.business.AccountBORepository;
import de.hatoka.account.capi.business.AccountBusinessFactory;
import de.hatoka.account.capi.business.UserBO;
import de.hatoka.account.capi.business.UserBORepository;
import de.hatoka.account.capi.dao.AccountDao;
import de.hatoka.account.capi.dao.UserDao;
import de.hatoka.account.capi.entities.AccountPO;
import de.hatoka.account.capi.entities.UserPO;
import de.hatoka.address.capi.business.AddressBO;
import de.hatoka.address.capi.business.AddressBOLifecycleListener;
import de.hatoka.address.capi.business.AddressBORepository;
import de.hatoka.address.capi.business.AddressBusinessFactory;
import de.hatoka.address.capi.doa.AddressDao;
import de.hatoka.address.capi.entities.AddressPO;
import de.hatoka.common.capi.exceptions.MandatoryParameterException;

public class AccountBusinessFactoryImpl implements AccountBusinessFactory
{
    @Inject
    private UserDao userDao;
    @Inject
    private AccountDao accountDao;
    @Inject
    private AddressDao addressDao;
    @Inject
    private AddressBusinessFactory addressBusinessFactory;
    @Inject
    private Injector injector;

    @Inject
    private AccountBusinessFactoryImpl()
    {

    }

    @Override
    public AccountBO getAccountBO(AccountPO accountPO)
    {
        if (accountPO == null)
        {
            throw new MandatoryParameterException("accountPO");
        }
        return new AccountBOImpl(accountPO, accountDao, this, addressDao);
    }

    @Override
    public AccountBORepository getAccountBORepository(UserPO userPO)
    {
        return new AccountBORepositoryImpl(accountDao, userPO, this);
    }

    @Override
    public AddressBO getAddressBO(AddressPO addressPO, AddressBOLifecycleListener lifecycleListener)
    {
        return addressBusinessFactory.createAddressBO(addressPO, lifecycleListener, addressDao);
    }

    @Override
    public AddressBORepository getAddressBORepository(UserPO userPO)
    {
        return new UserAddressBORespositoryImpl(userPO, addressDao, this);
    }

    @Override
    public UserBO getUserBO(UserPO userPO)
    {
        if (userPO == null)
        {
            throw new MandatoryParameterException("userPO");
        }

        UserBO accountUserBO = new UserBOImpl(userPO);
        injector.injectMembers(accountUserBO);
        return accountUserBO;
    }

    @Override
    public UserBORepository getUserBORepository()
    {
        return new UserBORepositoryImpl(userDao, this);
    }
}
