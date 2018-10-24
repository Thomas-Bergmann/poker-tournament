package de.hatoka.common.capi.persistence;

import java.io.Serializable;
import java.math.BigDecimal;

import jakarta.persistence.Embeddable;
import javax.validation.constraints.NotNull;

@Embeddable
public class QuantityPO implements Serializable
{
    private static final long serialVersionUID = 1L;
    @NotNull
    private String unit;
    @NotNull
    private BigDecimal amount;
    @NotNull
    private String unitScale;

    public QuantityPO()
    {
    }

    public QuantityPO(BigDecimal amount, String unit, String unitScale)
    {
        this.unit = unit;
        this.amount = amount;
        this.unitScale = unitScale;
    }

    public BigDecimal getAmount()
    {
        return amount;
    }

    public String getUnit()
    {
        return unit;
    }

    public String getUnitScale()
    {
        return unitScale;
    }

    public void setAmount(BigDecimal amount)
    {
        this.amount = amount;
    }

    public void setUnit(String unit)
    {
        this.unit = unit;
    }

    public void setUnitScale(String unitScale)
    {
        this.unitScale = unitScale;
    }
}
