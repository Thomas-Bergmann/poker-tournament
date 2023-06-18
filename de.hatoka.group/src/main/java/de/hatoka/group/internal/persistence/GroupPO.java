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
    name = "groups",
    uniqueConstraints={ @UniqueConstraint(columnNames= "group_ref") }
)
public class GroupPO implements Serializable
{
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "internal_id")
    private Long internalId;

    @NotNull
    @Column(name = "group_ref", nullable = false)
    private String globalGroupRef;

    @NotNull
    private String name;

    public GroupPO()
    {
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public String getGlobalGroupRef()
    {
        return globalGroupRef;
    }

    public void setGlobalGroupRef(String globalGroupRef)
    {
        this.globalGroupRef = globalGroupRef;
    }

    @Override
    public int hashCode()
    {
        return Objects.hash(globalGroupRef);
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
        GroupPO other = (GroupPO)obj;
        return Objects.equals(globalGroupRef, other.globalGroupRef);
    }
}
