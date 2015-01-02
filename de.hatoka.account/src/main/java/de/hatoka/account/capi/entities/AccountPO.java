package de.hatoka.account.capi.entities;

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

import de.hatoka.common.capi.dao.IdentifiableEntity;
import de.hatoka.common.capi.entities.MoneyPO;

@Entity
public class AccountPO implements Serializable, IdentifiableEntity
{
    private static final long serialVersionUID = 1L;

    @Id
    private String id;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "owner", updatable = false)
    private UserPO owner;

    private String name;

    private String addressRef;

    private boolean isActive;

    @Embedded
    @AttributeOverrides({ @AttributeOverride(name = "currencyCode", column = @Column(name = "revenueCurrencyCode")),
                    @AttributeOverride(name = "amount", column = @Column(name = "revenueAmount")) })
    private MoneyPO revenue;
    @Embedded
    @AttributeOverrides({ @AttributeOverride(name = "currencyCode", column = @Column(name = "deptCurrencyCode")),
                    @AttributeOverride(name = "amount", column = @Column(name = "deptAmount")) })
    private MoneyPO debt;

    public AccountPO()
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
        AccountPO other = (AccountPO)obj;
        if (id == null)
        {
            if (other.id != null)
                return false;
        }
        else if (!id.equals(other.id))
            return false;
        return true;
    }

    public String getAddressRef()
    {
        return addressRef;
    }

    public MoneyPO getDebt()
    {
        return debt;
    }

    @Override
    public String getId()
    {
        return id;
    }

    public String getName()
    {
        return name;
    }

    public UserPO getOwner()
    {
        return owner;
    }

    public MoneyPO getRevenue()
    {
        return revenue;
    }

    @Override
    public int hashCode()
    {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((id == null) ? 0 : id.hashCode());
        return result;
    }

    public boolean isActive()
    {
        return isActive;
    }

    public void setActive(boolean isActive)
    {
        this.isActive = isActive;
    }

    public void setAddressRef(String addressRef)
    {
        this.addressRef = addressRef;
    }

    public void setDebt(MoneyPO debt)
    {
        this.debt = debt;
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

    public void setOwner(UserPO owner)
    {
        // test account is equal
        if (owner == null)
        {
            if (this.owner == null)
            {
                return;
            }
        }
        else if (owner.equals(this.owner))
        {
            return;
        }
        // important set before set of users is updated
        UserPO old = this.owner;
        this.owner = owner;
        // update old and new owner
        if (old != null)
        {
            old.remove(this);
        }
        if (owner != null)
        {
            owner.add(this);
        }
    }

    public void setRevenue(MoneyPO revenue)
    {
        this.revenue = revenue;
    }
}
