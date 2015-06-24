package de.hatoka.tournament.capi.entities;

import java.io.Serializable;
import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlTransient;

import org.eclipse.persistence.oxm.annotations.XmlInverseReference;

import de.hatoka.common.capi.dao.IdentifiableEntity;

@Entity
public class RankPO implements Serializable, IdentifiableEntity
{
    private static final long serialVersionUID = 1L;

    @Id
    @XmlTransient
    private String id;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "tournament", updatable = false)
    @XmlInverseReference(mappedBy = "tournament")
    private TournamentPO tournament;

    @XmlAttribute
    private int firstPosition;
    @XmlAttribute
    private int lastPosition;
    @XmlAttribute
    @Column(precision=10, scale=4)
    private BigDecimal percentage; // 0.5 for 50%
    @XmlAttribute
    @Column(precision=10, scale=4)
    private BigDecimal amount;

    @Override
    @XmlTransient
    public String getId()
    {
        return id;
    }

    @Override
    public void setId(String id)
    {
        this.id = id;
    }

    @XmlTransient
    public int getFirstPosition()
    {
        return firstPosition;
    }

    public void setFirstPosition(int firstPosition)
    {
        this.firstPosition = firstPosition;
    }

    @XmlTransient
    public int getLastPosition()
    {
        return lastPosition;
    }

    public void setLastPosition(int lastPosition)
    {
        this.lastPosition = lastPosition;
    }

    @XmlTransient
    public BigDecimal getPercentage()
    {
        return percentage;
    }

    public void setPercentage(BigDecimal percentage)
    {
        this.percentage = percentage;
    }

    /**
     * Amount per player
     * @return
     */
    @XmlTransient
    public BigDecimal getAmount()
    {
        return amount;
    }

    public void setAmount(BigDecimal amount)
    {
        this.amount = amount;
    }

    @Override
    public int hashCode()
    {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((id == null) ? 0 : id.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj)
    {
        if (this == obj)
        {
            return true;
        }
        if (obj == null)
        {
            return false;
        }
        if (getClass() != obj.getClass())
        {
            return false;
        }
        RankPO other = (RankPO)obj;
        if (id == null)
        {
            if (other.id != null)
            {
                return false;
            }
        }
        else if (!id.equals(other.id))
        {
            return false;
        }
        return true;
    }

    @XmlTransient
    public TournamentPO getTournament()
    {
        return tournament;
    }

    public void setTournament(TournamentPO tournament)
    {
        this.tournament = tournament;
    }

}
