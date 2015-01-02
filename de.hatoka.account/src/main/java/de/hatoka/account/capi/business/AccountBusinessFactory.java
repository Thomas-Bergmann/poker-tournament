package de.hatoka.account.capi.business;

import de.hatoka.account.capi.entities.AccountPO;
import de.hatoka.account.capi.entities.UserPO;
import de.hatoka.address.capi.business.AddressBO;
import de.hatoka.address.capi.business.AddressBOLifecycleListener;
import de.hatoka.address.capi.business.AddressBORepository;
import de.hatoka.address.capi.entities.AddressPO;

public interface AccountBusinessFactory
{
    public AccountBO getAccountBO(AccountPO accountPO);

    public AccountBORepository getAccountBORepository(UserPO userPO);

    public AddressBO getAddressBO(AddressPO addressPO, AddressBOLifecycleListener lifecycleListener);

    public AddressBORepository getAddressBORepository(UserPO userPO);

    public UserBO getUserBO(UserPO userPO);

    public UserBORepository getUserBORepository();
}