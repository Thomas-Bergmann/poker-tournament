package de.hatoka.account.internal.app.models;

import javax.xml.bind.annotation.XmlRootElement;

import de.hatoka.account.internal.app.forms.LoginForm;
import de.hatoka.account.internal.app.forms.SignUpForm;

@XmlRootElement
public class LoginPageModel
{
    private LoginForm loginForm;
    private SignUpForm signUpForm;

    public LoginForm getLoginForm()
    {
        return loginForm;
    }

    public SignUpForm getSignUpForm()
    {
        return signUpForm;
    }

    public void setLoginForm(LoginForm loginForm)
    {
        this.loginForm = loginForm;
    }

    public void setSignUpForm(SignUpForm signUpForm)
    {
        this.signUpForm = signUpForm;
    }
}
