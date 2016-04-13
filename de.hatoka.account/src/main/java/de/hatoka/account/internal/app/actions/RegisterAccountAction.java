package de.hatoka.account.internal.app.actions;

import java.net.URI;

import javax.inject.Inject;

import de.hatoka.account.capi.business.UserBusinessFactory;
import de.hatoka.account.capi.business.UserBO;
import de.hatoka.account.internal.app.forms.SignUpForm;
import de.hatoka.common.capi.business.CountryHelper;

public class RegisterAccountAction
{
    private static final CountryHelper COUNTRY_HELPER = new CountryHelper();
    @Inject
    private UserBusinessFactory userBusinessFactory;

    private final URI action;

    public RegisterAccountAction(URI action)
    {
        this.action = action;
    }

    public void accept(SignUpForm form)
    {
        UserBO user = userBusinessFactory.getUserBORepository().createUserBO(form.getEmail());
        user.register(form.getEmail(), form.getPassword());
        user.setNickName(form.getName());
        user.setLocale(COUNTRY_HELPER.getLocale(form.getLocale()));
        user.sendEmailVerificationMail(action);
    }
}
