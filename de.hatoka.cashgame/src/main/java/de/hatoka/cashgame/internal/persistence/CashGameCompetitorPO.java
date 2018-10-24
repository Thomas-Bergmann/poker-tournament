package de.hatoka.cashgame.internal.persistence;

import java.io.Serializable;

import jakarta.persistence.AttributeOverride;
import jakarta.persistence.AttributeOverrides;
import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import javax.validation.constraints.NotNull;

import de.hatoka.cashgame.capi.types.CompetitorState;
import de.hatoka.common.capi.persistence.MoneyPO;

@Entity
@Table(
    name = "cashgame_competitor",
    uniqueConstraints={ @UniqueConstraint(columnNames= { "cashgame_id", "player_ref" }) }
)
public class CashGameCompetitorPO implements Serializable
{
    private static final long serialVersionUID = 1L;

    /**
     * Internal identifier for persistence only
     */
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "competitor_id")
    private Long id;

    @Column(name="player_ref")
    private String playerRef;

    @NotNull
    @Column(name="cashgame_id")
    private Long cashGameID;

    /**
     * The player is out and was placed at position
     */
    @Column(name="position")
    private Integer position;

    @Embedded
    @AttributeOverrides({ @AttributeOverride(name = "currencyCode", column = @Column(name = "invest_cur")),
                    @AttributeOverride(name = "amount", column = @Column(name = "invest_amount")) })
    private MoneyPO moneyInvest;

    @Embedded
    @AttributeOverrides({ @AttributeOverride(name = "currencyCode", column = @Column(name = "payout_cur")),
                    @AttributeOverride(name = "amount", column = @Column(name = "payout_amount")) })
    private MoneyPO moneyPayout;

    /**
     * state of player see {@link CompetitorState}
     */
    @NotNull
    @Column(name="state")
    private CompetitorState state = CompetitorState.INACTIVE;

    public CashGameCompetitorPO()
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
        CashGameCompetitorPO other = (CashGameCompetitorPO)obj;
        if (id == null)
        {
            if (other.id != null)
                return false;
        }
        else if (!id.equals(other.id))
            return false;
        return true;
    }

    public Long getId()
    {
        return id;
    }

    public Integer getPosition()
    {
        return position;
    }

    @Override
    public int hashCode()
    {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((id == null) ? 0 : id.hashCode());
        return result;
    }

    public void setId(Long id)
    {
        this.id = id;
    }

    public void setPosition(Integer position)
    {
        this.position = position;
    }

    public CompetitorState getState()
    {
        return state;
    }

    public void setState(CompetitorState state)
    {
        this.state = state;
    }

    public Long getCashGameID()
    {
        return cashGameID;
    }

    public void setCashGameID(Long cashgameID)
    {
        this.cashGameID = cashgameID;
    }

    public String getPlayerRef()
    {
        return playerRef;
    }

    public void setPlayerRef(String playerRef)
    {
        this.playerRef = playerRef;
    }

    public MoneyPO getMoneyInvest()
    {
        return moneyInvest;
    }

    public void setMoneyInvest(MoneyPO moneyInvest)
    {
        this.moneyInvest = moneyInvest;
    }

    public MoneyPO getMoneyPayout()
    {
        return moneyPayout;
    }

    public void setMoneyPayout(MoneyPO moneyPayout)
    {
        this.moneyPayout = moneyPayout;
    }
}
