package de.hatoka.common.capi.persistence;

import de.hatoka.common.capi.math.Money;

public class MoneyPOConverter
{
    public static Money valueOf(MoneyPO po)
    {
        return po == null ? Money.NOTHING : Money.valueOf(po.getAmount(), po.getCurrencyCode());
    }

    public static MoneyPO persistence(Money value)
    {
        return value == null || Money.NOTHING.equals(value) ? null : new MoneyPO(value.getAmount(), value.getCurrency().getCurrencyCode());
    }
}
