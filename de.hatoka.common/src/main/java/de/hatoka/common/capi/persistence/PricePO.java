package de.hatoka.common.capi.persistence;

import java.io.Serializable;

import jakarta.persistence.Embeddable;
import javax.validation.constraints.NotNull;

@Embeddable
public class PricePO implements Serializable
{
    private static final long serialVersionUID = 1L;
    @NotNull
    private MoneyPO amount;
    @NotNull
    private QuantityPO quantity;

    public PricePO()
    {
    }

    public PricePO(MoneyPO amount, QuantityPO quantity)
    {
        this.amount = amount;
        this.quantity = quantity;
    }

    public MoneyPO getAmount()
    {
        return amount;
    }

    public QuantityPO getQuantity()
    {
        return quantity;
    }

    public void setAmount(MoneyPO amount)
    {
        this.amount = amount;
    }

    public void setQuantity(QuantityPO quantity)
    {
        this.quantity = quantity;
    }
}
