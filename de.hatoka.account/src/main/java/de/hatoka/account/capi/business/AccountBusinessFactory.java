package de.hatoka.account.capi.business;

import de.hatoka.account.capi.entities.AccountPO;
import de.hatoka.account.capi.entities.UserPO;

public interface AccountBusinessFactory
{
    public AccountBO getAccountBO(AccountPO accountPO);

    public AccountBORepository getAccountBORepository(UserPO userPO);

    public UserBO getUserBO(UserPO userPO);

    public UserBORepository getUserBORepository();
}