package de.hatoka.account.internal.app.actions;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;

import de.hatoka.account.capi.business.AccountBO;
import de.hatoka.account.capi.business.UserBO;
import de.hatoka.account.internal.app.models.AccountListModel;
import de.hatoka.account.internal.app.models.AccountVO;

public class AccountAction
{
    private final UserBO userBO;

    public AccountAction(UserBO userBO)
    {
        this.userBO = userBO;
    }

    public AccountBO createAccountBO(String accountName)
    {
        AccountBO account = userBO.getAccountBORepository().createAccountBO();
        account.setName(accountName);
        return account;
    }

    public void deleteAccounts(final List<String> accountIDs, String selectedAccountID)
    {
        for (String accountID : accountIDs)
        {
            // account is not selected
            if (!accountID.equals(selectedAccountID))
            {
                AccountBO accountBO = getAccountBO(accountID);
                if (accountBO != null)
                {
                    accountBO.remove();
                }
            }
        }
    }

    public AccountBO getAccountBO(String accountID)
    {
        List<AccountBO> accounts = userBO.getAccountBORepository().getAccountBOs();
        Optional<AccountBO> optionalAccountBO = accounts.stream()
                        .filter(anyAccount -> anyAccount.getID().equals(accountID)).findAny();
        return optionalAccountBO.isPresent() ? optionalAccountBO.get() : null;
    }

    public AccountListModel getListModel(final String selectedAccountID)
    {
        final AccountListModel accounts = new AccountListModel();
        List<AccountBO> accountBOs = userBO.getAccountBORepository().getAccountBOs();
        Consumer<AccountBO> mapper = new Consumer<AccountBO>()
        {
            @Override
            public void accept(AccountBO accountBO)
            {
                AccountVO result = new AccountVO(accountBO);
                result.setSelected(result.getId().equals(selectedAccountID));
                accounts.getAccounts().add(result);
            }
        };
        Comparator<AccountBO> sorter = new Comparator<AccountBO>()
        {

            @Override
            public int compare(AccountBO a, AccountBO b)
            {
                if (a.getName() == null)
                {
                    return -1;
                }
                return a.getName().compareTo(b.getName());
            }
        };
        accountBOs.sort(sorter);
        accountBOs.forEach(mapper);
        return accounts;
    }

}
