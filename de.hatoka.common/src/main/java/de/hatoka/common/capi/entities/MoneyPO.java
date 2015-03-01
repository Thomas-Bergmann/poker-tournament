package de.hatoka.common.capi.entities;

import java.io.Serializable;
import java.math.BigDecimal;

import javax.persistence.Embeddable;
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

@Embeddable
@XmlRootElement
public class MoneyPO implements Serializable
{
    private static final long serialVersionUID = 1L;
    @NotNull
    @XmlAttribute(name="code")
    private String currencyCode;
    @NotNull
    @XmlAttribute
    private String amount;

    public MoneyPO()
    {
    }

    public MoneyPO(BigDecimal amount, String currencyCode)
    {
        this.currencyCode = currencyCode;
        this.amount = amount.toString();
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
        MoneyPO other = (MoneyPO)obj;
        if (amount == null)
        {
            if (other.amount != null)
                return false;
        }
        else if (!amount.equals(other.amount))
            return false;
        if (currencyCode == null)
        {
            if (other.currencyCode != null)
                return false;
        }
        else if (!currencyCode.equals(other.currencyCode))
            return false;
        return true;
    }

    public BigDecimal getAmount()
    {
        return new BigDecimal(amount);
    }

    public String getCurrencyCode()
    {
        return currencyCode;
    }

    @Override
    public int hashCode()
    {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((amount == null) ? 0 : amount.hashCode());
        result = prime * result + ((currencyCode == null) ? 0 : currencyCode.hashCode());
        return result;
    }
}
