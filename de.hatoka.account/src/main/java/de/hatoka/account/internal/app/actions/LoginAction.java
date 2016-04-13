package de.hatoka.account.internal.app.actions;

import javax.inject.Inject;

import de.hatoka.account.capi.business.UserBO;
import de.hatoka.account.capi.business.UserBORepository;
import de.hatoka.account.capi.business.UserBusinessFactory;
import de.hatoka.account.internal.app.forms.LoginForm;

public class LoginAction
{
    @Inject
    private UserBusinessFactory userBusinessFactory;

    public void accept(LoginForm form)
    {
        UserBORepository repository = userBusinessFactory.getUserBORepository();
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
    }
}
