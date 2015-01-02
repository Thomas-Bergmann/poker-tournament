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

import de.hatoka.common.capi.dao.IdentifiableEntity;

@Entity
@NamedQueries(value = {
                @NamedQuery(name = "PlayerPO.findByAccountRef", query = "select a from PlayerPO a where a.accountRef = :accountRef"),
                @NamedQuery(name = "PlayerPO.findByName", query = "select a from PlayerPO a where a.name = :name")
})
public class PlayerPO implements Serializable, IdentifiableEntity
{
    private static final long serialVersionUID = 1L;

    @Id
    private String id;
    @NotNull
    private String accountRef;
    @NotNull
    private String name;
    @OneToMany(mappedBy = "player")
    private Set<CompetitorPO> competitors = new HashSet<>();

    public PlayerPO()
    {
    }

    public String getAccountRef()
    {
        return accountRef;
    }

    public Set<CompetitorPO> getCompetitors()
    {
        return competitors;
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

}
