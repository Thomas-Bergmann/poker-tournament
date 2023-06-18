package de.hatoka.common.capi.math;

import java.math.BigDecimal;
import java.util.Currency;

/**
 * A Price represents the amount of money for a given quantity
 */
public class Price
{
    private final Money amount;

    private final Quantity quantity;

    /**
     * Simple constructor for pieces
     *
     * @param one
     */
    public Price(BigDecimal amount, Currency currency)
    {
        this(Money.valueOf(amount, currency), Quantity.ONE_PCS);
    }

    public Price(Money amount, Quantity quantity)
    {
        this.amount = amount;
        this.quantity = quantity;
    }

    public Price divide(BigDecimal divisor)
    {
        return new Price(amount.divide(divisor), quantity);
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
        Price other = (Price)obj;
        if (amount == null)
        {
            if (other.amount != null)
                return false;
        }
        else if (!amount.equals(other.amount))
            return false;
        if (quantity == null)
        {
            if (other.quantity != null)
                return false;
        }
        else if (!quantity.equals(other.quantity))
            return false;
        return true;
    }

    public Money getMoney()
    {
        return amount;
    }

    public Price getPriceFor(Quantity newQuantity)
    {
        return new Price(amount.multiply(newQuantity.divide(quantity)), newQuantity);
    }

    public Quantity getQuantity()
    {
        return quantity;
    }

    @Override
    public int hashCode()
    {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((amount == null) ? 0 : amount.hashCode());
        result = prime * result + ((quantity == null) ? 0 : quantity.hashCode());
        return result;
    }

    public Price multiply(BigDecimal multiplicand)
    {
        return new Price(amount.multiply(multiplicand), quantity);
    }

    @Override
    public String toString()
    {
        return "Price [amount=" + amount + ", quantity=" + quantity + "]";
    }

}
