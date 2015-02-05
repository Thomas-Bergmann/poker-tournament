package de.hatoka.account.internal.app.actions;

import java.util.List;

import javax.inject.Inject;

import de.hatoka.account.capi.business.AccountBO;
import de.hatoka.account.capi.business.AccountBusinessFactory;
import de.hatoka.account.capi.business.UserBO;
import de.hatoka.account.capi.business.UserBORepository;
import de.hatoka.account.internal.app.forms.LoginForm;

public class LoginAction
{
    @Inject
    private AccountBusinessFactory accountBusinessFactory;

    public void accept(LoginForm form)
    {
        UserBORepository repository = accountBusinessFactory.getUserBORepository();
        UserBO userBO = repository.getUserBOByLogin(form.getEmail());
        if (userBO == null)
        {
            form.setLoginFailed(true);
            return;
        }
        if (!userBO.verifyPassword(form.getPassword()))
        {
            form.setLoginFailed(true);
            return;
        }
        form.setLoginFailed(false);
        form.setUserID(userBO.getID());
        List<AccountBO> accounts = userBO.getAccountBORepository().getAccountBOs();
        if (accounts.isEmpty())
        {
            // TODO later accounts = userBO.getAssignedAccountBOs();
        }
        if (accounts.isEmpty())
        {
            throw new RuntimeException("User needs a least one personal account");
        }
        form.setAccountID(accounts.get(0).getID());
    }
}
