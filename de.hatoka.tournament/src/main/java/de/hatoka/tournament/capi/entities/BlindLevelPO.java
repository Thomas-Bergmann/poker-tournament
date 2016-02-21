package de.hatoka.tournament.capi.entities;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlTransient;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import org.eclipse.persistence.oxm.annotations.XmlInverseReference;

import de.hatoka.common.capi.app.xslt.DateXmlAdapter;
import de.hatoka.common.capi.dao.IdentifiableEntity;

@Entity
public class BlindLevelPO implements Serializable, IdentifiableEntity
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
    private Integer smallBlind;

    @XmlAttribute
    private Integer bigBlind;

    @XmlAttribute
    private int position;

    @XmlAttribute
    private Integer ante;

    @NotNull
    @XmlAttribute
    private boolean isReBuy= false;

    @NotNull
    @XmlAttribute
    private boolean isPause = false;

    /**
     * Duration in minutes
     */
    @NotNull
    @XmlAttribute
    private Integer duration;

    @Temporal(TemporalType.TIMESTAMP)
    @XmlAttribute
    @XmlJavaTypeAdapter(DateXmlAdapter.class)
    private Date startDate;

    @NotNull
    @XmlAttribute
    private boolean isActive = false;

    public BlindLevelPO()
    {

    }

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
    public TournamentPO getTournamentPO()
    {
        return tournament;
    }

    public void setTournamentPO(TournamentPO tournament)
    {
        this.tournament = tournament;
    }

    @XmlTransient
    public Integer getSmallBlind()
    {
        return smallBlind;
    }

    public void setSmallBlind(Integer smallBlind)
    {
        this.smallBlind = smallBlind;
    }

    @XmlTransient
    public Integer getBigBlind()
    {
        return bigBlind;
    }

    public void setBigBlind(Integer bigBlind)
    {
        this.bigBlind = bigBlind;
    }

    @XmlTransient
    public Integer getAnte()
    {
        return ante;
    }

    public void setAnte(Integer ante)
    {
        this.ante = ante;
    }

    @XmlTransient
    public boolean isPause()
    {
        return isPause;
    }

    public void setPause(boolean isPause)
    {
        this.isPause = isPause;
    }

    @XmlTransient
    public Integer getDuration()
    {
        return duration;
    }

    public void setDuration(Integer duration)
    {
        this.duration = duration;
    }

    @XmlTransient
    public Integer getPosition()
    {
        return position;
    }

    public void setPosition(Integer position)
    {
        this.position = position;
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
        BlindLevelPO other = (BlindLevelPO)obj;
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
    public boolean isReBuy()
    {
        return isReBuy;
    }

    public void setReBuy(boolean isRebuy)
    {
        this.isReBuy = isRebuy;
    }

    @XmlTransient
    public Date getStartDate()
    {
        return startDate;
    }

    public void setStartDate(Date startDate)
    {
        this.startDate = startDate;
    }

    @XmlTransient
    public boolean isActive()
    {
        return isActive;
    }

    public void setActive(boolean isActive)
    {
        this.isActive = isActive;
    }

}
