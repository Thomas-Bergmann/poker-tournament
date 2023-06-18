package de.hatoka.cashgame.internal.business;

import java.util.Comparator;

import de.hatoka.cashgame.capi.business.CompetitorBO;
import de.hatoka.common.capi.math.Comparators;

public final class CashGameComparators
{
    private CashGameComparators()
    {
    }

    public static final Comparator<CompetitorBO> RESULT = new Comparator<CompetitorBO>()
    {
        @Override
        public int compare(CompetitorBO o1, CompetitorBO o2)
        {
            if (o1 == null || o2 == null)
            {
                return Comparators.NULL.compare(o1, o2);
            }
            // largest first
            int result = Comparators.MONEY.compare(o2.getResult(), o1.getResult());
            if (result == 0)
            {
                if( o1.isActive() != o2.isActive())
                {
                    result = o1.isActive() ? -1 : 1;
                }
            }
            return result;
        }
    };

    public static final Comparator<CompetitorBO> NAME = new Comparator<CompetitorBO>()
    {
        @Override
        public int compare(CompetitorBO o1, CompetitorBO o2)
        {
            if (o1 == null || o2 == null)
            {
                return Comparators.NULL.compare(o1, o2);
            }
            return Comparators.STRING.compare(o1.getPlayer().getName(), o2.getPlayer().getName());
        }
    };
}
