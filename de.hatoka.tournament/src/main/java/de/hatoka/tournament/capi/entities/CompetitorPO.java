package de.hatoka.tournament.capi.entities;

import java.io.Serializable;

import javax.persistence.AttributeOverride;
import javax.persistence.AttributeOverrides;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlID;
import javax.xml.bind.annotation.XmlTransient;

import org.eclipse.persistence.oxm.annotations.XmlInverseReference;

import de.hatoka.common.capi.dao.IdentifiableEntity;
import de.hatoka.common.capi.entities.MoneyPO;

@Entity
public class CompetitorPO implements Serializable, IdentifiableEntity
{
    private static final long serialVersionUID = 1L;

    @Id
    @XmlAttribute
    @XmlID
    private String id;

    @NotNull
    @Column(updatable = false)
    @XmlTransient
    private String accountRef;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "player", updatable = false)
    private PlayerPO player;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "tournament", updatable = false)
    @XmlInverseReference(mappedBy="tournament")
    private TournamentPO tournament;

    /**
     * The player is in play
     */
    @XmlAttribute(name="active")
    private boolean isActive = false;

    /**
     * The player is out and was placed at position
     */
    @XmlAttribute
    private Integer positition;

    @Embedded
    @AttributeOverrides({ @AttributeOverride(name = "currencyCode", column = @Column(name = "inplayCur")),
                    @AttributeOverride(name = "amount", column = @Column(name = "inplayAmount")) })
    @XmlElement(name="inplay")
    private MoneyPO moneyInPlay;

    @Embedded
    @AttributeOverrides({ @AttributeOverride(name = "currencyCode", column = @Column(name = "resultCur")),
                    @AttributeOverride(name = "amount", column = @Column(name = "resultAmount")) })
    @XmlElement(name="result")
    private MoneyPO moneyResult;

    public CompetitorPO()
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
        CompetitorPO other = (CompetitorPO)obj;
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

    @Override
    @XmlTransient
    public String getId()
    {
        return id;
    }

    @XmlTransient
    public MoneyPO getMoneyInPlay()
    {
        return moneyInPlay;
    }

    @XmlTransient
    public MoneyPO getMoneyResult()
    {
        return moneyResult;
    }

    @XmlAttribute(name="playerRef")
    public String getPlayerRef()
    {
        return player.getId();
    }

    @XmlTransient
    public PlayerPO getPlayerPO()
    {
        return player;
    }

    @XmlTransient
    public Integer getPositition()
    {
        return positition;
    }

    @XmlTransient
    public TournamentPO getTournamentPO()
    {
        return tournament;
    }

    @Override
    public int hashCode()
    {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((id == null) ? 0 : id.hashCode());
        return result;
    }

    @XmlTransient
    public boolean isActive()
    {
        return isActive;
    }

    public void setAccountRef(String accountRef)
    {
        this.accountRef = accountRef;
    }

    public void setActive(boolean isActive)
    {
        this.isActive = isActive;
    }

    @Override
    public void setId(String id)
    {
        this.id = id;
    }

    public void setMoneyInPlay(MoneyPO moneyInPlay)
    {
        this.moneyInPlay = moneyInPlay;
    }

    public void setMoneyResult(MoneyPO moneyResult)
    {
        this.moneyResult = moneyResult;
    }

    public void setPlayerPO(PlayerPO playerPO)
    {
        player = playerPO;
    }

    public void setPositition(Integer positition)
    {
        this.positition = positition;
    }

    public void setTournamentPO(TournamentPO tournamentPO)
    {
        tournament = tournamentPO;
    }
}
