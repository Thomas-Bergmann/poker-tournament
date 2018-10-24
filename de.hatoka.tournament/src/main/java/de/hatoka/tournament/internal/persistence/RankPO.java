package de.hatoka.tournament.internal.persistence;

import java.io.Serializable;
import java.math.BigDecimal;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "tournament_rank")
public class RankPO implements Serializable
{
    private static final long serialVersionUID = 1L;

    /**
     * Internal identifier for persistence only
     */
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "rank_id")
    private Long id;

    @NotNull
    @Column(name="tournament_id", nullable = false)
    private Long tournamentID;

    @NotNull
    @Column(name="position_first", nullable = false)
    private int firstPosition;
    @NotNull
    @Column(name="position_last", nullable = false)
    private int lastPosition;
    @Column(precision = 10, scale = 4)
    private BigDecimal percentage; // 0.5 for 50%
    @Column(precision = 10, scale = 4)
    private BigDecimal amount;

    public Long getId()
    {
        return id;
    }

    public void setId(Long id)
    {
        this.id = id;
    }

    public int getFirstPosition()
    {
        return firstPosition;
    }

    public void setFirstPosition(int firstPosition)
    {
        this.firstPosition = firstPosition;
    }

    public int getLastPosition()
    {
        return lastPosition;
    }

    public void setLastPosition(int lastPosition)
    {
        this.lastPosition = lastPosition;
    }

    public BigDecimal getPercentage()
    {
        return percentage;
    }

    public void setPercentage(BigDecimal percentage)
    {
        this.percentage = percentage;
    }

    /**
     * @return amount of money per player in this rank
     */
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

    public Long getTournamentID()
    {
        return tournamentID;
    }

    public void setTournamentID(Long tournamentID)
    {
        this.tournamentID = tournamentID;
    }
}
