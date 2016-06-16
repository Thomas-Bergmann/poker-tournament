package de.hatoka.group.capi.entities;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlTransient;

import de.hatoka.common.capi.dao.IdentifiableEntity;

@Entity
@NamedQueries(value = {
                @NamedQuery(name = "MemberPO.findByUserRef", query = "select a from MemberPO a where a.userRef = :userRef")
})
public class MemberPO implements Serializable, IdentifiableEntity
{
    private static final long serialVersionUID = 1L;

    @Id
    @XmlTransient
    private String id;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "groupPO", updatable = false)
    private GroupPO groupPO;

    /**
     * user reference of member
     */
    @NotNull
    @XmlAttribute
    private String userRef;

    /**
     * name of user inside of the group
     */
    @NotNull
    @XmlAttribute
    private String name;

    public MemberPO()
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
        MemberPO other = (MemberPO)obj;
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
    @XmlTransient
    public String getId()
    {
        return id;
    }

    @XmlTransient
    public GroupPO getGroup()
    {
        return groupPO;
    }

    public void setGroup(GroupPO groupPO)
    {
        this.groupPO = groupPO;
    }

    @XmlTransient
    public String getUserRef()
    {
        return userRef;
    }

    public void setUserRef(String userRef)
    {
        this.userRef = userRef;
    }

    @Override
    public void setId(String id)
    {
        this.id = id;
    }

    @XmlTransient
    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }
}
