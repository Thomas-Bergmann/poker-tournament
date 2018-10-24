package de.hatoka.tournament.capi.business;

import java.util.Comparator;

import de.hatoka.common.capi.math.Comparators;

public final class CompetitorComparators
{
    private CompetitorComparators()
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
            return Comparators.MONEY.compare(o1.getResult(), o2.getResult());
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
