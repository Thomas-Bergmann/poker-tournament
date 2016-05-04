package de.hatoka.group.capi.entities;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlTransient;

import de.hatoka.common.capi.dao.IdentifiableEntity;

@Entity
@NamedQueries(value = {
                @NamedQuery(name = "GroupPO.findByName", query = "select a from GroupPO a where a.name = :name"),
                @NamedQuery(name = "GroupPO.findByOwnerRef", query = "select a from GroupPO a where a.ownerRef = :ownerRef")
})
public class GroupPO implements Serializable, IdentifiableEntity
{
    private static final long serialVersionUID = 1L;

    @Id
    @XmlTransient
    private String id;

    @NotNull
    private String name;

    @NotNull
    private String ownerRef;

    @OneToMany(mappedBy = "groupPO", cascade = CascadeType.ALL)
    @XmlElement(name = "member")
    private List<MemberPO> members = new ArrayList<>();

    public GroupPO()
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
        GroupPO other = (GroupPO)obj;
        if (id == null)
        {
            if (other.id != null)
                return false;
        }
        else if (!id.equals(other.id))
            return false;
        return true;
    }

    @Override
    public String getId()
    {
        return id;
    }

    @Override
    public void setId(String id)
    {
        this.id = id;
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public List<MemberPO> getMembers()
    {
        return members;
    }

    public void setMembers(List<MemberPO> members)
    {
        this.members = members;
    }

    public String getOwnerRef()
    {
        return ownerRef;
    }

    public void setOwnerRef(String ownerRef)
    {
        this.ownerRef = ownerRef;
    }
}
