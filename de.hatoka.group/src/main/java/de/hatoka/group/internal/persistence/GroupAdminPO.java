package de.hatoka.group.internal.persistence;

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
    name="group_admin",
    uniqueConstraints={ @UniqueConstraint(columnNames= {"group_ref", "user_ref"}) }
)
public class GroupAdminPO implements Serializable
{
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "groupadmin_id")
    private long internalId;

    @NotNull
    @Column(name = "group_ref", nullable = false)
    private String groupRef;

    /**
     * player reference of member
     */
    @NotNull
    @Column(name = "user_ref", nullable = false)
    private String userRef;

    public GroupAdminPO()
    {

    }

    public String getGroupRef()
    {
        return groupRef;
    }

    public void setGroupRef(String groupRef)
    {
        this.groupRef = groupRef;
    }

    public String getUserRef()
    {
        return userRef;
    }

    public void setUserRef(String userRef)
    {
        this.userRef = userRef;
    }

    @Override
    public int hashCode()
    {
        return Objects.hash(groupRef, userRef);
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
        GroupAdminPO other = (GroupAdminPO)obj;
        return Objects.equals(groupRef, other.groupRef) && Objects.equals(userRef, other.userRef);
    }
}
