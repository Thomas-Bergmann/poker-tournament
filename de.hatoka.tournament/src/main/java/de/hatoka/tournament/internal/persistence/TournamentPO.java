package de.hatoka.tournament.internal.persistence;

import java.io.Serializable;
import java.util.Date;
import java.util.Objects;

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
import jakarta.persistence.UniqueConstraint;
import javax.validation.constraints.NotNull;

import de.hatoka.common.capi.persistence.MoneyPO;

@Entity
@Table(
    name = "tournaments",
    uniqueConstraints={ @UniqueConstraint(columnNames= { "owner_ref", "local_ref" }) }
)
public class TournamentPO implements Serializable
{
    private static final long serialVersionUID = 1L;

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "tournament_id")
    private Long internalID;

    @NotNull
    @Column(name = "owner_ref")
    private String ownerRef;

    /**
     * External ID of a tournament to be able to change the name of a tournament
     */
    @NotNull
    @Column(name = "local_ref")
    private String localRef;

    @NotNull
    @Column(name = "name", nullable = false)
    private String name;

    @NotNull
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "date_start", nullable = false)
    private Date startDate;

    @NotNull
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "date_end", nullable = true)
    private Date endDate;

    @Embedded
    @AttributeOverrides({ @AttributeOverride(name = "currencyCode", column = @Column(name = "buyInCur")),
                    @AttributeOverride(name = "amount", column = @Column(name = "buyInAmount")) })
    private MoneyPO buyIn;

    @Embedded
    @AttributeOverrides({ @AttributeOverride(name = "currencyCode", column = @Column(name = "reBuyCur")),
                    @AttributeOverride(name = "amount", column = @Column(name = "reBuyAmount")) })
    private MoneyPO reBuy;

    @NotNull
    private int maxPlayerPerTable = 10;

    @NotNull
    private int initialStacksize = 1000;

    @NotNull
    private int currentRound = -1;

    /**
     * Tournament or CashGame is an event of the group
     */
    @Column(name = "group_ref")
    private String groupRef;

    public TournamentPO()
    {
    }

    public Long getInternalID()
    {
        return internalID;
    }

    public String getOwnerRef()
    {
        return ownerRef;
    }

    public void setOwnerRef(String ownerRef)
    {
        this.ownerRef = ownerRef;
    }

    public String getLocalRef()
    {
        return localRef;
    }

    public void setLocalRef(String localRef)
    {
        this.localRef = localRef;
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public Date getStartDate()
    {
        return startDate;
    }

    public void setStartDate(Date startDate)
    {
        this.startDate = startDate;
    }

    public Date getEndDate()
    {
        return endDate;
    }

    public void setEndDate(Date endDate)
    {
        this.endDate = endDate;
    }

    public MoneyPO getBuyIn()
    {
        return buyIn;
    }

    public void setBuyIn(MoneyPO buyIn)
    {
        this.buyIn = buyIn;
    }

    public MoneyPO getReBuy()
    {
        return reBuy;
    }

    public void setReBuy(MoneyPO reBuy)
    {
        this.reBuy = reBuy;
    }

    public int getMaxPlayerPerTable()
    {
        return maxPlayerPerTable;
    }

    public void setMaxPlayerPerTable(int maxPlayerPerTable)
    {
        this.maxPlayerPerTable = maxPlayerPerTable;
    }

    public int getInitialStacksize()
    {
        return initialStacksize;
    }

    public void setInitialStacksize(int initialStacksize)
    {
        this.initialStacksize = initialStacksize;
    }

    public int getCurrentRound()
    {
        return currentRound;
    }

    public void setCurrentRound(int currentRound)
    {
        this.currentRound = currentRound;
    }

    public String getGroupRef()
    {
        return groupRef;
    }

    public void setGroupRef(String groupRef)
    {
        this.groupRef = groupRef;
    }

    @Override
    public int hashCode()
    {
        return Objects.hash(localRef, ownerRef);
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
        return Objects.equals(localRef, other.localRef) && Objects.equals(ownerRef, other.ownerRef);
    }
}
