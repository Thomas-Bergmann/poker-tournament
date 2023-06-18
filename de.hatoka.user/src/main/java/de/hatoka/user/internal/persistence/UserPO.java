package de.hatoka.user.internal.persistence;

import java.io.Serializable;
import java.util.Objects;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import javax.validation.constraints.NotNull;

@Entity
@Table(
    name = "users",
    uniqueConstraints={ @UniqueConstraint(columnNames= "user_ref") }
)
public class UserPO implements Serializable
{
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long internalId;

    @NotNull
    @Column(name = "user_ref", nullable = false)
    private String globalRef;

    /**
     * email retrieved via authentication provider
     */
    private String email;

    /**
     * User is active and can login with password
     */
    @NotNull
    @Column(name = "active", nullable = false)
    private boolean isActive = false;
    @Column(name = "name_nick")
    private String nickName;
    @Column(name = "name_first")
    private String firstName;
    @Column(name = "name_last")
    private String lastName;
    @Column(name = "locale")
    private String locale;
    @Column(name = "time_zone")
    private String timeZone;

    public Long getInternalId()
    {
        return internalId;
    }

    public String getGlobalRef()
    {
        return globalRef;
    }

    public void setGlobalRef(String globalRef)
    {
        this.globalRef = globalRef;
    }

    public String getEmail()
    {
        return email;
    }

    public void setEmail(String email)
    {
        this.email = email;
    }

    public boolean isActive()
    {
        return isActive;
    }

    public void setActive(boolean isActive)
    {
        this.isActive = isActive;
    }

    public String getNickName()
    {
        return nickName;
    }

    public void setNickName(String nickName)
    {
        this.nickName = nickName;
    }

    public String getFirstName()
    {
        return firstName;
    }

    public void setFirstName(String firstName)
    {
        this.firstName = firstName;
    }

    public String getLastName()
    {
        return lastName;
    }

    public void setLastName(String lastName)
    {
        this.lastName = lastName;
    }

    public String getLocale()
    {
        return locale;
    }

    public void setLocale(String locale)
    {
        this.locale = locale;
    }

    public String getTimeZone()
    {
        return timeZone;
    }

    public void setTimeZone(String timeZone)
    {
        this.timeZone = timeZone;
    }

    @Override
    public int hashCode()
    {
        return Objects.hash(globalRef);
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
        return Objects.equals(globalRef, other.globalRef);
    }
}
