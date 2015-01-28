package de.hatoka.tournament.capi.entities;

import java.io.Serializable;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.AttributeOverride;
import javax.persistence.AttributeOverrides;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;

import de.hatoka.common.capi.dao.IdentifiableEntity;
import de.hatoka.common.capi.entities.MoneyPO;

@Entity
@NamedQuery(name = "TournamentPO.findByAccountRef", query = "select a from TournamentPO a where a.accountRef = :accountRef")
public class TournamentPO implements Serializable, IdentifiableEntity
{
    private static final long serialVersionUID = 1L;

    @Id
    private String id;
    @NotNull
    private String accountRef;
    @NotNull
    private String name;
    @NotNull
    @Temporal(TemporalType.DATE)
    private Date date;

    @Embedded
    @AttributeOverrides({ @AttributeOverride(name = "currencyCode", column = @Column(name = "buyInCur")),
                    @AttributeOverride(name = "amount", column = @Column(name = "buyInAmount")) })
    private MoneyPO buyIn;

    @OneToMany(mappedBy = "tournament")
    private Set<CompetitorPO> competitors = new HashSet<>();

    public TournamentPO()
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
        TournamentPO other = (TournamentPO)obj;
        if (id == null)
        {
            if (other.id != null)
                return false;
        }
        else if (!id.equals(other.id))
            return false;
        return true;
    }

    public String getAccountRef()
    {
        return accountRef;
    }

    public MoneyPO getBuyIn()
    {
        return buyIn;
    }

    public Set<CompetitorPO> getCompetitors()
    {
        return competitors;
    }

    public Date getDate()
    {
        return date;
    }

    @Override
    public String getId()
    {
        return id;
    }

    public String getName()
    {
        return name;
    }

    @Override
    public int hashCode()
    {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((id == null) ? 0 : id.hashCode());
        return result;
    }

    public void setAccountRef(String accountRef)
    {
        this.accountRef = accountRef;
    }

    public void setBuyIn(MoneyPO buyIn)
    {
        this.buyIn = buyIn;
    }

    public void setCompetitors(Set<CompetitorPO> competitors)
    {
        this.competitors = competitors;
    }

    public void setDate(Date date)
    {
        this.date = date;
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
}
