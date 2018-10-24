package de.hatoka.tournament.internal.persistence;

import java.io.Serializable;
import java.util.Date;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import jakarta.persistence.UniqueConstraint;
import javax.validation.constraints.NotNull;

@Entity
@Table(
    name = "tournament_blindlevel",
    uniqueConstraints={ @UniqueConstraint(columnNames= { "tournament_id", "position" }) }
)
public class BlindLevelPO implements Serializable
{
    private static final long serialVersionUID = 1L;

    /**
     * Internal identifier for persistence only
     */
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "blindlevel_id")
    private Long id;

    @NotNull
    @Column(name="tournament_id", nullable = false)
    private Long tournamentID;

    @NotNull
    @Column(name="position", nullable = false)
    private int position;

    @Column(name="blind_small")
    private Integer smallBlind;

    @Column(name="blind_big")
    private Integer bigBlind;

    @Column(name="ante")
    private Integer ante;

    @NotNull
    @Column(name="rebuy_allowed", nullable = false)
    private boolean isReBuy = false;

    @NotNull
    @Column(name="pause", nullable = false)
    private boolean isPause = false;

    /**
     * Duration in minutes
     */
    @NotNull
    @Column(name="duration")
    private Integer duration;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name="date_start")
    private Date startDate;

    public BlindLevelPO()
    {

    }

    public Long getId()
    {
        return id;
    }

    public Long getTournamentID()
    {
        return tournamentID;
    }

    public void setTournamentID(Long tournamentID)
    {
        this.tournamentID = tournamentID;
    }

    public Integer getSmallBlind()
    {
        return smallBlind;
    }

    public void setSmallBlind(Integer smallBlind)
    {
        this.smallBlind = smallBlind;
    }

    public Integer getBigBlind()
    {
        return bigBlind;
    }

    public void setBigBlind(Integer bigBlind)
    {
        this.bigBlind = bigBlind;
    }

    public Integer getAnte()
    {
        return ante;
    }

    public void setAnte(Integer ante)
    {
        this.ante = ante;
    }

    public boolean isPause()
    {
        return isPause;
    }

    public void setPause(boolean isPause)
    {
        this.isPause = isPause;
    }

    public Integer getDuration()
    {
        return duration;
    }

    public void setDuration(Integer duration)
    {
        this.duration = duration;
    }

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

    public boolean isReBuy()
    {
        return isReBuy;
    }

    public void setReBuy(boolean isRebuy)
    {
        this.isReBuy = isRebuy;
    }

    public Date getStartDate()
    {
        return startDate;
    }

    public void setStartDate(Date startDate)
    {
        this.startDate = startDate;
    }
}
