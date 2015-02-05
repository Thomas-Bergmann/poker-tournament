package de.hatoka.account.internal.app.forms;

import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlRootElement;

import de.hatoka.common.capi.app.FormHelper;

@XmlRootElement
public class LoginForm
{
    private static final FormHelper HELPER = new FormHelper();

    @NotNull
    private String email;
    @NotNull
    private String password;

    /**
     * URI String, where the user came from
     */
    private String origin;

    private boolean isNowActive = false;
    private boolean isLoginFailed = false;
    private boolean fillMandatoryFields = false;

    /**
     * user id after successful login
     */
    private String userID;

    /**
     * account id after successful login
     */
    private String accountID;

    public String getAccountID()
    {
        return accountID;
    }

    public String getOrigin()
    {
        return origin;
    }

    public String getPassword()
    {
        return password;
    }

    public String getUserID()
    {
        return userID;
    }

    public boolean isFillMandatoryFields()
    {
        return fillMandatoryFields;
    }

    public boolean isLoginFailed()
    {
        return isLoginFailed;
    }

    public boolean isNowActive()
    {
        return isNowActive;
    }

    public void setAccountID(String accountID)
    {
        this.accountID = accountID;
    }

    public void setFillMandatoryFields(boolean fillMandatoryFields)
    {
        this.fillMandatoryFields = fillMandatoryFields;
    }

    public void setLoginFailed(boolean isLoginFailed)
    {
        this.isLoginFailed = isLoginFailed;
    }

    public void setNowActive(boolean isNowActive)
    {
        this.isNowActive = isNowActive;
    }

    public void setOrigin(String origin)
    {
        this.origin = origin;
    }

    public void setPassword(String password)
    {
        this.password = password;
    }

    public void setUserID(String userID)
    {
        this.userID = userID;
    }

    public boolean validate()
    {
        if (HELPER.isEmpty(getEmail(), password))
        {
            fillMandatoryFields = true;
            return false;
        }
        return true;
    }

    public String getEmail()
    {
        return email;
    }

    public void setEmail(String email)
    {
        this.email = email;
    }
}
