package de.hatoka.tournament.capi.entities;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.AttributeOverride;
import javax.persistence.AttributeOverrides;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlID;
import javax.xml.bind.annotation.XmlTransient;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import de.hatoka.common.capi.app.xslt.DateXmlAdapter;
import de.hatoka.common.capi.dao.IdentifiableEntity;
import de.hatoka.common.capi.entities.MoneyPO;

@Entity
@NamedQueries(value = {
                @NamedQuery(name = "TournamentPO.findByAccountRef", query = "select a from TournamentPO a where a.accountRef = :accountRef"),
                @NamedQuery(name = "TournamentPO.findByExternalRef", query = "select a from TournamentPO a where a.accountRef = :accountRef and a.externalRef = :externalRef") })
public class TournamentPO implements Serializable, IdentifiableEntity
{
    private static final long serialVersionUID = 1L;

    @Id
    @XmlTransient
    private String id;

    @NotNull
    @XmlID
    @XmlAttribute(name = "id")
    private String externalRef;

    @NotNull
    private String accountRef;
    @NotNull
    @XmlAttribute
    private boolean isCashGame;
    @NotNull
    private String name;

    @NotNull
    @Temporal(TemporalType.DATE)
    @XmlAttribute
    @XmlJavaTypeAdapter(DateXmlAdapter.class)
    private Date date;

    @Embedded
    @AttributeOverrides({ @AttributeOverride(name = "currencyCode", column = @Column(name = "buyInCur")),
                    @AttributeOverride(name = "amount", column = @Column(name = "buyInAmount")) })
    private MoneyPO buyIn;

    @OneToMany(mappedBy = "tournament", cascade=CascadeType.ALL)
    @XmlElement(name = "blindLevel")
    private List<BlindLevelPO> blindLevels = new ArrayList<>();

    @OneToMany(mappedBy = "tournament", cascade=CascadeType.ALL)
    @XmlElement(name = "rank")
    private List<RankPO> ranks = new ArrayList<>();

    @OneToMany(mappedBy = "tournament")
    @XmlElement(name = "competitor")
    private Set<CompetitorPO> competitors = new HashSet<>();

    @OneToMany(mappedBy = "tournament")
    @XmlElement(name = "historyEntry")
    private List<HistoryPO> historyEntries = new ArrayList<>();

    @NotNull
    @XmlAttribute
    private int maxPlayerPerTable = 10;

    @NotNull
    @XmlAttribute
    private int initialStacksize= 1000;

    @NotNull
    @XmlAttribute
    private int currentRound = 0;

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

    @XmlTransient
    public String getAccountRef()
    {
        return accountRef;
    }

    public MoneyPO getBuyIn()
    {
        return buyIn;
    }

    @XmlTransient
    public Set<CompetitorPO> getCompetitors()
    {
        return competitors;
    }

    @XmlTransient
    public Date getDate()
    {
        return date;
    }

    @XmlTransient
    @Override
    public String getId()
    {
        return id;
    }

    @XmlTransient
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

    @XmlTransient
    public boolean isCashGame()
    {
        return isCashGame;
    }

    public void setCashGame(boolean isCashGame)
    {
        this.isCashGame = isCashGame;
    }

    @XmlTransient
    public List<HistoryPO> getHistoryEntries()
    {
        return historyEntries;
    }

    public void setHistoryEntries(List<HistoryPO> historyEntries)
    {
        this.historyEntries = historyEntries;
    }

    @XmlTransient
    public List<BlindLevelPO> getBlindLevels()
    {
        return blindLevels;
    }

    public void setBlindLevels(List<BlindLevelPO> blindLevels)
    {
        this.blindLevels = blindLevels;
    }

    @XmlTransient
    public int getMaxPlayerPerTable()
    {
        return maxPlayerPerTable;
    }

    public void setMaxPlayerPerTable(int maxPlayerPerTable)
    {
        this.maxPlayerPerTable = maxPlayerPerTable;
    }

    @XmlTransient
    public int getCurrentRound()
    {
        return currentRound;
    }

    public void setCurrentRound(int currentRound)
    {
        this.currentRound = currentRound;
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
    public List<RankPO> getRanks()
    {
        return ranks;
    }

    public void setRanks(List<RankPO> ranks)
    {
        this.ranks = ranks;
    }

    @XmlTransient
    public int getInitialStacksize()
    {
        return initialStacksize;
    }

    public void setInitialStacksize(int initialStacksize)
    {
        this.initialStacksize = initialStacksize;
    }
}
