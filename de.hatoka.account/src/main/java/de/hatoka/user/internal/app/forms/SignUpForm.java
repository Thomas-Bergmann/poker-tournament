package de.hatoka.user.internal.app.forms;

import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlRootElement;

import de.hatoka.common.capi.app.FormHelper;

@XmlRootElement
public class SignUpForm
{
    private static final FormHelper HELPER = new FormHelper();

    @NotNull
    private String name;
    @NotNull
    private String email;
    @NotNull
    private String password;

    private String locale = "en_US";
    private boolean fillMandatoryFields = false;
    private Boolean emailExists;
    private boolean successfullyRegistered = false;
    private boolean tokenValidationFailed = false;

    public String getEmail()
    {
        return email;
    }

    public Boolean getEmailExists()
    {
        return emailExists;
    }

    public Boolean getFillMandatoryFields()
    {
        return fillMandatoryFields;
    }

    public String getLocale()
    {
        return locale;
    }

    public String getName()
    {
        return name;
    }

    public String getPassword()
    {
        return password;
    }

    public boolean isSuccessfullyRegistered()
    {
        return successfullyRegistered;
    }

    public boolean isTokenValidationFailed()
    {
        return tokenValidationFailed;
    }

    public void setEmail(String email)
    {
        this.email = email;
    }

    public void setEmailExists(Boolean emailExists)
    {
        this.emailExists = emailExists;
    }

    public void setFillMandatoryFields(Boolean fillMandatoryFields)
    {
        this.fillMandatoryFields = fillMandatoryFields;
    }

    public void setLocale(String locale)
    {
        this.locale = locale;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public void setPassword(String password)
    {
        this.password = password;
    }

    public void setSuccessfullyRegistered(boolean successfullyRegistered)
    {
        this.successfullyRegistered = successfullyRegistered;
    }

    public void setTokenValidationFailed(boolean tokenValidationFailed)
    {
        this.tokenValidationFailed = tokenValidationFailed;
    }

    public boolean validate()
    {
        if (HELPER.isEmpty(name, email, password))
        {
            setFillMandatoryFields(true);
            return false;
        }
        return true;
    }
}
