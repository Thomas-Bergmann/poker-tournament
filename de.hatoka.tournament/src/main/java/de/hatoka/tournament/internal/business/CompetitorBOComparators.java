package de.hatoka.tournament.internal.business;

import java.util.Comparator;

import de.hatoka.common.capi.business.Comparators;
import de.hatoka.tournament.capi.business.CompetitorBO;

public final class CompetitorBOComparators
{
    private CompetitorBOComparators()
    {
    }

    public static final Comparator<CompetitorBO> CASH_GAME = new Comparator<CompetitorBO>()
    {
        @Override
        public int compare(CompetitorBO o1, CompetitorBO o2)
        {
            // inactive last
            int result = ACTIVE.reversed().compare(o1, o2);
            if (result == 0)
            {
                // first with most amount in result
                result = RESULT.reversed().compare(o1, o2);
            }
            if (result == 0)
            {
                // first with less amount in play
                result = INPLAY.compare(o1, o2);
            }
            return result;
        }
    };

    public static final Comparator<CompetitorBO> TOURNAMENT = new Comparator<CompetitorBO>()
    {
        @Override
        public int compare(CompetitorBO o1, CompetitorBO o2)
        {
            // inactive last
            int result = POSITION.reversed().compare(o1, o2);
            if (result == 0)
            {
                // first with most amount in result
                result = NAME.compare(o1, o2);
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

    public static final Comparator<CompetitorBO> INPLAY = new Comparator<CompetitorBO>()
    {
        @Override
        public int compare(CompetitorBO o1, CompetitorBO o2)
        {
            if (o1 == null || o2 == null)
            {
                return Comparators.NULL.compare(o1, o2);
            }
            return Comparators.MONEY.compare(o1.getInPlay(), o2.getInPlay());
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
            return Comparators.MONEY.compare(o1.getResult(), o2.getResult());
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
            return Comparators.BOOLEAN.compare(o1.isActive(), o2.isActive());
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
