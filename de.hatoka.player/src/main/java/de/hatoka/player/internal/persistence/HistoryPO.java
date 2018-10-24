package de.hatoka.player.internal.persistence;

import java.io.Serializable;
import java.util.Date;

import jakarta.persistence.AttributeOverride;
import jakarta.persistence.AttributeOverrides;
import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import javax.validation.constraints.NotNull;

import de.hatoka.common.capi.persistence.MoneyPO;
import de.hatoka.player.capi.types.HistoryEntryType;

@Entity
@Table(name = "player_history")
public class HistoryPO implements Serializable
{
    private static final long serialVersionUID = 1L;

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "history_id")
    private Long id;

    @NotNull
    @Column(name = "player_ref", nullable = false)
    private String playerRef;

    @NotNull
    @Column(name="game_ref", nullable = false)
    private String gameRef;

    /**
     * type of action
     */
    @NotNull
    @Column(name="type", nullable = false)
    private HistoryEntryType type;

    @NotNull
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name="date", nullable = false)
    private Date date;

    @Embedded
    @AttributeOverrides({ @AttributeOverride(name = "currencyCode", column = @Column(name = "amountCur")),
                    @AttributeOverride(name = "amount", column = @Column(name = "amount")) })
    private MoneyPO amount;

    public HistoryPO()
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
        HistoryPO other = (HistoryPO)obj;
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
    public String toString()
    {
        return "{ player=" + playerRef + ", game=" + gameRef + ", type=" + type + ", date=" + date
                        + ", amount=" + amount + "}";
    }

    public Long getId()
    {
        return id;
    }

    public MoneyPO getAmount()
    {
        return amount;
    }

    @Override
    public int hashCode()
    {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((id == null) ? 0 : id.hashCode());
        return result;
    }

    public void setAmount(MoneyPO moneyAmount)
    {
        this.amount = moneyAmount;
    }

    public Date getDate()
    {
        return date;
    }

    public void setDate(Date date)
    {
        this.date = date;
    }

    public String getGameRef()
    {
        return gameRef;
    }

    public void setGameRef(String gameRef)
    {
        this.gameRef = gameRef;
    }

    public String getPlayerRef()
    {
        return playerRef;
    }

    public void setPlayerRef(String playerRef)
    {
        this.playerRef = playerRef;
    }

    public HistoryEntryType getType()
    {
        return type;
    }

    public void setType(HistoryEntryType type)
    {
        this.type = type;
    }
}
