package de.hatoka.tournament.internal.business;

import java.util.Comparator;

import de.hatoka.common.capi.business.Comparators;
import de.hatoka.tournament.capi.business.CompetitorBO;

public final class CompetitorBOComparators
{
    private CompetitorBOComparators()
    {
    }

    public static final Comparator<CompetitorBO> DEFAULT = new Comparator<CompetitorBO>()
    {
        @Override
        public int compare(CompetitorBO o1, CompetitorBO o2)
        {
            int result = ACTIVE.compare(o1, o2);
            if (result == 0)
            {
                result = RESULT.compare(o1, o2);
            }
            return result;
        }
    };

    public static final Comparator<CompetitorBO> POSITION = new Comparator<CompetitorBO>()
    {
        @Override
        public int compare(CompetitorBO o1, CompetitorBO o2)
        {
            if (o1 == null || o2 == null)
            {
                return Comparators.NULL.compare(o1, o2);
            }
            return Comparators.INTEGER.compare(o1.getPosition(), o2.getPosition());
        }
    };

    public static final Comparator<CompetitorBO> RESULT = new Comparator<CompetitorBO>()
    {
        @Override
        public int compare(CompetitorBO o1, CompetitorBO o2)
        {
            if (o1 == null || o2 == null)
            {
                return Comparators.NULL.compare(o1, o2);
            }
            return Comparators.MONEY.compare(o2.getResult(), o1.getResult());
        }
    };

    public static final Comparator<CompetitorBO> ACTIVE = new Comparator<CompetitorBO>()
    {
        @Override
        public int compare(CompetitorBO o1, CompetitorBO o2)
        {
            if (o1 == null || o2 == null)
            {
                return Comparators.NULL.compare(o1, o2);
            }
            return Comparators.BOOLEAN.compare(o1.isActive(), o1.isActive());
        }
    };
}
