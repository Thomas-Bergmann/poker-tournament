package de.hatoka.common.capi.app.model;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlTransient;

import de.hatoka.common.capi.business.Money;

public class MoneyVO
{
    @XmlAttribute
    private String amount;
    @XmlAttribute
    private String currencyCode;

    public MoneyVO()
    {
    }

    public MoneyVO(Money money)
    {
        amount = money.getAmount().stripTrailingZeros().toPlainString();
        currencyCode = money.getCurrency().getCurrencyCode();
    }

    @XmlTransient
    public String getAmount()
    {
        return amount;
    }

    @XmlTransient
    public String getCurrencyCode()
    {
        return currencyCode;
    }

    public void setAmount(String amount)
    {
        this.amount = amount;
    }

    public void setCurrencyCode(String currencyCode)
    {
        this.currencyCode = currencyCode;
    }

    public Money toMoney()
    {
        return Money.valueOf(amount, currencyCode);
    }
}
