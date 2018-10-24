package de.hatoka.cashgame.internal.business;

import java.util.Comparator;

import de.hatoka.cashgame.capi.business.CashGameBO;

public final class CashGameBOComparators
{
    private CashGameBOComparators()
    {
    }

    public static final Comparator<CashGameBO> DEFAULT_CASHGAME = new Comparator<CashGameBO>()
    {
        @Override
        public int compare(CashGameBO a, CashGameBO b)
        {
            return a.getDate().compareTo(b.getDate());
        }
    };
}
