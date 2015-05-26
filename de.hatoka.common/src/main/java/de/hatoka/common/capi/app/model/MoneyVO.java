package de.hatoka.common.capi.app.model;

import de.hatoka.common.capi.business.Money;

public class MoneyVO
{
    private String amount;
    private String currencyCode;

    public MoneyVO()
    {
    }

    public MoneyVO(Money money)
    {
        amount = money.getAmount().stripTrailingZeros().toPlainString();
        currencyCode = money.getCurrency().getCurrencyCode();
    }

    public String getAmount()
    {
        return amount;
    }

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
