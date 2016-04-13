package de.hatoka.user.internal.app.actions;

import java.net.URI;

import javax.inject.Inject;

import de.hatoka.common.capi.business.CountryHelper;
import de.hatoka.user.capi.business.UserBO;
import de.hatoka.user.capi.business.UserBORepository;
import de.hatoka.user.capi.business.UserBusinessFactory;
import de.hatoka.user.internal.app.forms.LoginForm;
import de.hatoka.user.internal.app.forms.SignUpForm;

public class LoginAction
{
    private static final CountryHelper COUNTRY_HELPER = new CountryHelper();
    @Inject
    private UserBusinessFactory userBusinessFactory;

    public void accept(URI uri, SignUpForm form)
    {
        UserBO user = userBusinessFactory.getUserBORepository().createUserBO(form.getEmail());
        user.register(form.getEmail(), form.getPassword());
        user.setNickName(form.getName());
        user.setLocale(COUNTRY_HELPER.getLocale(form.getLocale()));
        user.sendEmailVerificationMail(uri);
    }

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
