package de.hatoka.cashgame.internal.persistence;

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
    name = "cashgames",
    uniqueConstraints={ @UniqueConstraint(columnNames= { "owner_ref", "local_ref" }) }
)
public class CashGamePO implements Serializable
{
    private static final long serialVersionUID = 1L;

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "cashgame_id", nullable = false)
    private Long internalID;

    @NotNull
    @Column(name = "owner_ref", nullable = false)
    private String ownerRef;

    /**
     * External ID of a tournament to be able to change the name of a tournament
     */
    @NotNull
    @Column(name = "local_ref", nullable = false)
    private String localRef;

    @NotNull
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "date_start", nullable = false)
    private Date startDate;

    @Embedded
    @AttributeOverrides({ @AttributeOverride(name = "currencyCode", column = @Column(name = "buyInCur")),
                    @AttributeOverride(name = "amount", column = @Column(name = "buyInAmount")) })
    private MoneyPO buyIn;

    /**
     * Tournament or CashGame is an event of the group
     */
    @Column(name = "group_ref")
    private String groupRef;

    public CashGamePO()
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

    public Date getStartDate()
    {
        return startDate;
    }

    public void setStartDate(Date startDate)
    {
        this.startDate = startDate;
    }

    public MoneyPO getBuyIn()
    {
        return buyIn;
    }

    public void setBuyIn(MoneyPO buyIn)
    {
        this.buyIn = buyIn;
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
        CashGamePO other = (CashGamePO)obj;
        return Objects.equals(localRef, other.localRef) && Objects.equals(ownerRef, other.ownerRef);
    }
}
