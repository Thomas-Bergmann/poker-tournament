package de.hatoka.user.capi.entities;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.validation.constraints.NotNull;

import de.hatoka.common.capi.dao.IdentifiableEntity;

@Entity
@NamedQuery(name = "UserPO.findByLogin", query = "select a from UserPO a where a.login = :login")
public class UserPO implements Serializable, IdentifiableEntity
{
    private static final long serialVersionUID = 1L;
    @Id
    private String id;
    @NotNull
    private String login;

    /**
     * email is same as login for front end created users.
     */
    private String email;

    /**
     * is email verified during sign up or later email change? In that case a
     * user can be active, but the email could not be verified.
     */
    private boolean emailIsVerified = false;

    /**
     * User is active and can login with password
     */
    private boolean isActive = false;

    /**
     * User must provide this token to activate his account
     */
    private String signInToken;

    private String password;
    private String nickName;
    private String firstName;
    private String lastName;
    private String locale;
    private String timeZone;

    public UserPO()
    {
    }

    @Override
    public boolean equals(Object obj)
    {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        UserPO other = (UserPO)obj;
        if (id == null)
        {
            if (other.id != null)
                return false;
        }
        else if (!id.equals(other.id))
            return false;
        return true;
    }

    public String getEmail()
    {
        return email;
    }

    public String getFirstName()
    {
        return firstName;
    }

    @Override
    public String getId()
    {
        return id;
    }

    public String getLastName()
    {
        return lastName;
    }

    public String getLocale()
    {
        return locale;
    }

    public String getLogin()
    {
        return login;
    }

    public String getNickName()
    {
        return nickName;
    }

    public String getPassword()
    {
        return password;
    }

    public String getSignInToken()
    {
        return signInToken;
    }

    @Override
    public int hashCode()
    {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((id == null) ? 0 : id.hashCode());
        return result;
    }

    public boolean isActive()
    {
        return isActive;
    }

    public boolean isEmailIsVerified()
    {
        return emailIsVerified;
    }

    public void setActive(boolean isActive)
    {
        this.isActive = isActive;
    }

    public void setEmail(String email)
    {
        this.email = email;
    }

    public void setEmailIsVerified(boolean emailIsVerified)
    {
        this.emailIsVerified = emailIsVerified;
    }

    public void setFirstName(String firstName)
    {
        this.firstName = firstName;
    }

    @Override
    public void setId(String id)
    {
        this.id = id;
    }

    public void setLastName(String lastName)
    {
        this.lastName = lastName;
    }

    public void setLocale(String locale)
    {
        this.locale = locale;
    }

    public void setLogin(String login)
    {
        this.login = login;
    }

    public void setNickName(String nickName)
    {
        this.nickName = nickName;
    }

    public void setPassword(String password)
    {
        this.password = password;
    }

    public void setSignInToken(String signInToken)
    {
        this.signInToken = signInToken;
    }

    public String getTimeZone()
    {
        return timeZone;
    }

    public void setTimeZone(String timeZone)
    {
        this.timeZone = timeZone;
    }

}
