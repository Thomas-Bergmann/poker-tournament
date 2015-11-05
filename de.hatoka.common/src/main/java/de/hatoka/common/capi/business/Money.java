package de.hatoka.common.capi.business;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Currency;

import de.hatoka.common.capi.entities.MoneyPO;
import de.hatoka.common.capi.exceptions.MandatoryParameterException;

public class Money
{
    public static Money valueOf(MoneyPO money)
    {
        if (money == null)
        {
            return Money.NOTHING;
        }
        return new Money(money.getAmount(), Currency.getInstance(money.getCurrencyCode()));
    }

    public static Money valueOf(String amount)
    {
        if (amount == null || amount.isEmpty())
        {
            return Money.NOTHING;
        }
        String[] splitAmount = amount.split("\\s+");
        return valueOf(splitAmount[0], splitAmount[1]);
    }

    public static Money valueOf(String amount, Currency currency)
    {
        return new Money(new BigDecimal(amount), currency);
    }

    public static Money valueOf(BigDecimal amount, String currency)
    {
        return new Money(amount, Currency.getInstance(currency));
    }

    public static Money valueOf(BigDecimal amount, Currency currency)
    {
        return new Money(amount, currency);
    }

    public static Money valueOf(String amount, String currencyCode)
    {
        return new Money(new BigDecimal(amount), Currency.getInstance(currencyCode));
    }

    public static final Money NOTHING = new Money(BigDecimal.ZERO, CurrencyConstants.USD);

    public static final Money ONE_USD = new Money(BigDecimal.ONE, CurrencyConstants.USD);

    private final Currency currency;

    private final BigDecimal amount;

    private static boolean isZero(Money amount)
    {
        if (amount == null)
        {
            return true;
        }
        return BigDecimal.ZERO.equals(amount.getAmount().stripTrailingZeros());
    }

    private Money(BigDecimal amount, Currency currency)
    {
        if (amount == null)
        {
            throw new MandatoryParameterException("amount");
        }
        if (currency == null)
        {
            throw new MandatoryParameterException("currency");
        }
        this.currency = currency;
        this.amount = amount;
    }

    public boolean isZero()
    {
        return isZero(this);
    }

    /**
     * Adds two Money values of the same currency code
     *
     * @param augend
     * @return a Price whose value is (this + augend), and whose scale is
     *         max(this.getAmount().scale(), augend.getAmount().scale()).
     */
    public Money add(Money augend)
    {
        if (isZero(this))
        {
            return augend;
        }
        if (isZero(augend))
        {
            return this;
        }
        if (currency != augend.getCurrency())
        {
            throw new IllegalArgumentException("Only money of the same currency can be added.");
        }
        return new Money(amount.add(augend.getAmount()), currency);
    }

    /**
     * Divide the amount with an additional scale of 4.
     * (e.g. 10.divide(3) == 3.3333)
     *
     * @param divisor
     * @return the divisor with an additional scale of 4
     */
    public Money divide(BigDecimal divisor)
    {
        return divide(divisor, 4);
    }

    /**
     * Divide the amount with an additional scale. The given scale will be added
     * to positive scale of amount or divisor. Negative scales will be ignored.
     *
     * @param divisor
     * @param additionalScale
     * @return the rounded divisor
     */
    public Money divide(BigDecimal divisor, int additionalScale)
    {
        if (isZero(this))
        {
            return this;
        }
        return new Money(amount.setScale(getPositiveScale(amount) + getPositiveScale(divisor) + additionalScale).divide(divisor, RoundingMode.HALF_EVEN), currency);
    }

    private static int getPositiveScale(BigDecimal value)
    {
        return value.scale() < 0 ? 0 : value.scale();
    }

    public Money divide(long divisor)
    {
        if (divisor == 1)
        {
            return this;
        }
        return divide(BigDecimal.valueOf(divisor));
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
        Money other = (Money)obj;
        if (amount == null)
        {
            if (other.amount != null)
                return false;
        }
        else if (!amount.stripTrailingZeros().equals(other.amount.stripTrailingZeros()))
            return false;
        if (isZero(this) && isZero(other))
        {
            return true;
        }
        if (!currency.equals(other.currency))
            return false;
        return true;
    }

    public BigDecimal getAmount()
    {
        return amount;
    }

    public Currency getCurrency()
    {
        return currency;
    }

    public Money round(RoundingMode mode)
    {
        return new Money(amount.setScale(currency.getDefaultFractionDigits(), mode).stripTrailingZeros(), currency);
    }

    public Money round()
    {
        return round(RoundingMode.HALF_EVEN);
    }

    public Money stripTrailingZeros()
    {
        return new Money(amount.stripTrailingZeros(), currency);
    }

    @Override
    public int hashCode()
    {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((amount == null) ? 0 : amount.stripTrailingZeros().hashCode());
        result = prime * result + currency.getNumericCode();
        return result;
    }

    public Money multiply(BigDecimal multiplicand)
    {
        return new Money(amount.multiply(multiplicand), currency);
    }

    public Money negate()
    {
        return new Money(getAmount().negate(), getCurrency());
    }

    public Money subtract(Money subtrahend)
    {
        if (isZero(this))
        {
            return subtrahend.negate();
        }
        if (isZero(subtrahend))
        {
            return this;
        }
        if (currency != subtrahend.getCurrency())
        {
            throw new IllegalArgumentException("Only money of the same currency can be subtracted.");
        }
        return new Money(amount.subtract(subtrahend.getAmount()), currency);
    }

    public MoneyPO toMoneyPO()
    {
        return new MoneyPO(getAmount(), getCurrency().getCurrencyCode());
    }

    @Override
    public String toString()
    {
        return "Money [" + amount + " " + currency.getCurrencyCode() + "]";
    }
}
