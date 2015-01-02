package de.hatoka.common.capi.business;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Currency;

import de.hatoka.common.capi.entities.MoneyPO;
import de.hatoka.common.capi.exceptions.MandatoryParameterException;

public class Money
{
    public static Money getInstance(MoneyPO money)
    {
        if (money == null)
        {
            return Money.NOTHING;
        }
        return new Money(money.getAmount(), Currency.getInstance(money.getCurrencyCode()));
    }

    public static Money getInstance(String amount)
    {
        if (amount == null || amount.isEmpty())
        {
            return Money.NOTHING;
        }
        String[] splitAmount = amount.split("\\s+");
        return getInstance(splitAmount[0], splitAmount[1]);
    }

    public static Money getInstance(String amount, Currency currency)
    {
        return new Money(new BigDecimal(amount), currency);
    }

    public static Money getInstance(String amount, String currencyCode)
    {
        Currency currency = Currency.getInstance(currencyCode);
        return getInstance(amount, currency);
    }

    public static final Money NOTHING = new Money(BigDecimal.ZERO, CurrencyConstants.USD);

    public static final Money ONE_USD = new Money(BigDecimal.ONE, CurrencyConstants.USD);

    private final Currency currency;

    private final BigDecimal amount;

    public Money(BigDecimal amount, Currency currency)
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

    /**
     * Adds two Money values of the same currency code
     *
     * @param augend
     * @return a Price whose value is (this + augend), and whose scale is
     *         max(this.getAmount().scale(), augend.getAmount().scale()).
     */
    public Money add(Money augend)
    {
        if (BigDecimal.ZERO.equals(getAmount()))
        {
            return augend;
        }
        if (augend == null || BigDecimal.ZERO.equals(augend.getAmount().stripTrailingZeros()))
        {
            return this;
        }
        if (currency != augend.getCurrency())
        {
            throw new IllegalArgumentException("Only money of the same currency can be added.");
        }
        return new Money(amount.add(augend.getAmount()), currency);
    }

    public Money divide(BigDecimal divisor)
    {
        if (BigDecimal.ZERO.equals(getAmount().stripTrailingZeros()))
        {
            return this;
        }
        return new Money(amount.setScale(amount.scale() + divisor.scale() + 2).divide(divisor, RoundingMode.HALF_EVEN),
                        currency);
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
        else if (!getTrimmedAmount().equals(other.getTrimmedAmount()))
            return false;
        if (currency != other.currency)
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

    public BigDecimal getTrimmedAmount()
    {
        return amount.stripTrailingZeros();
    }

    @Override
    public int hashCode()
    {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((amount == null) ? 0 : getTrimmedAmount().hashCode());
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
