package de.hatoka.common.capi.math;

import java.math.BigDecimal;

import de.hatoka.common.capi.exceptions.MandatoryParameterException;

/**
 * Quantity represents an amount and a unit of measurement. E.g. 5 pcs or
 * 523.245 kilo gram
 */
public class Quantity
{
    public static final Quantity ONE_PCS = new Quantity(BigDecimal.ONE);
    public static final Quantity TEN_PCS = new Quantity(BigDecimal.TEN);

    private final Unit unit;
    private final BigDecimal amount;
    private final UnitScale scale;

    /**
     * Simplified constructor for a number of pieces.
     *
     * @param amount
     */
    public Quantity(BigDecimal amount)
    {
        this(amount, UnitScale.ONE, Unit.PCS);
    }

    public Quantity(BigDecimal amount, Unit unit)
    {
        this(amount, UnitScale.ONE, unit);
    }

    /**
     * Creates an instance of Quantity e.g. ten milli gram
     *
     * @param amount
     *            {@link BigDecimal#TEN}
     * @param scale
     *            {@link UnitScale#MILLI}
     * @param unit
     *            {@link Unit#GRAM}
     */
    public Quantity(BigDecimal amount, UnitScale scale, Unit unit)
    {
        if (unit == null)
        {
            throw new MandatoryParameterException("unit");
        }
        if (amount == null)
        {
            throw new MandatoryParameterException("amount");
        }
        if (scale == null)
        {
            throw new MandatoryParameterException("scale");
        }
        this.unit = unit;
        this.amount = amount;
        this.scale = scale;
    }

    public BigDecimal divide(Quantity divisor)
    {
        if (!unit.equals(divisor.unit))
        {
            throw new IllegalArgumentException(
                            "Can't mix two different units " + unit.name() + " and " + divisor.unit.name());
        }
        // 700 g div 1 kg
        return amount.multiply(scale.getBaseValue()).divide(divisor.amount).divide(divisor.scale.getBaseValue());
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
        Quantity other = (Quantity)obj;
        if (amount == null)
        {
            if (other.amount != null)
                return false;
        }
        else if (!getTrimmedAmount().equals(other.getTrimmedAmount()))
            return false;
        if (unit == null)
        {
            if (other.unit != null)
                return false;
        }
        else if (!unit.equals(other.unit))
            return false;
        return true;
    }

    public BigDecimal getAmount()
    {
        return amount;
    }

    public UnitScale getScale()
    {
        return scale;
    }

    public BigDecimal getTrimmedAmount()
    {
        return amount.stripTrailingZeros();
    }

    public Unit getUnit()
    {
        return unit;
    }

    @Override
    public int hashCode()
    {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((amount == null) ? 0 : getTrimmedAmount().hashCode());
        result = prime * result + ((unit == null) ? 0 : unit.hashCode());
        return result;
    }

    @Override
    public String toString()
    {
        return amount.toString() + scale.toString() + unit.toString();
    }

}
