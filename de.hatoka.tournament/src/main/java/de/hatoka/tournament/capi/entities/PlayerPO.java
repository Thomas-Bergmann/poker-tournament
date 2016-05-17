package de.hatoka.tournament.capi.entities;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlID;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

import de.hatoka.common.capi.dao.IdentifiableEntity;

@Entity
@NamedQueries(value = {
                @NamedQuery(name = "PlayerPO.findByAccountRef", query = "select a from PlayerPO a where a.accountRef = :accountRef"),
                @NamedQuery(name = "PlayerPO.findByName", query = "select a from PlayerPO a where a.accountRef = :accountRef and a.name = :name"),
                @NamedQuery(name = "PlayerPO.findByExternalRef", query = "select a from PlayerPO a where a.accountRef = :accountRef and a.externalRef = :externalRef")
})
@XmlRootElement(name="player")
public class PlayerPO implements Serializable, IdentifiableEntity
{
    private static final long serialVersionUID = 1L;

    @XmlTransient
    @Id
    private String id;

    @NotNull
    @XmlID
    @XmlAttribute(name="id")
    private String externalRef;

    @NotNull
    private String accountRef;

    @NotNull
    @XmlAttribute
    private String name;

    @XmlAttribute
    private String eMail;

    @Override
    public int hashCode()
    {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((accountRef == null) ? 0 : accountRef.hashCode());
        result = prime * result + ((name == null) ? 0 : name.hashCode());
        return result;
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
        PlayerPO other = (PlayerPO)obj;
        if (accountRef == null)
        {
            if (other.accountRef != null)
                return false;
        }
        else if (!accountRef.equals(other.accountRef))
            return false;
        if (name == null)
        {
            if (other.name != null)
                return false;
        }
        else if (!name.equals(other.name))
            return false;
        return true;
    }

    @OneToMany(mappedBy = "playerRef")
    @XmlTransient
    private Set<CompetitorPO> competitors = new HashSet<>();

    public PlayerPO()
    {
    }

    @XmlTransient
    public String getAccountRef()
    {
        return accountRef;
    }

    @XmlTransient
    public Set<CompetitorPO> getCompetitors()
    {
        return competitors;
    }

    @Override
    @XmlTransient
    public String getId()
    {
        return id;
    }

    @XmlTransient
    public String getName()
    {
        return name;
    }

    public void setAccountRef(String accountRef)
    {
        this.accountRef = accountRef;
    }

    public void setCompetitors(Set<CompetitorPO> competitors)
    {
        this.competitors = competitors;
    }

    @Override
    public void setId(String id)
    {
        this.id = id;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    @XmlTransient
    public String getExternalRef()
    {
        return externalRef;
    }

    public void setExternalRef(String externalRef)
    {
        this.externalRef = externalRef;
    }

    @XmlTransient
    public String getEMail()
    {
        return eMail;
    }

    public void setEMail(String eMail)
    {
        this.eMail = eMail;
    }

}
