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
import de.hatoka.common.capi.exceptions.MandatoryParameterException;

public class AccountBusinessFactoryImpl implements AccountBusinessFactory
{
    @Inject
    private UserDao userDao;
    @Inject
    private AccountDao accountDao;
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
        return new AccountBOImpl(accountPO, accountDao, this);
    }

    @Override
    public AccountBORepository getAccountBORepository(UserPO userPO)
    {
        return new AccountBORepositoryImpl(accountDao, userPO, this);
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
