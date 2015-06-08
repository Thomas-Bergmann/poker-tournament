package de.hatoka.tournament.internal.business;

import java.util.Comparator;

import de.hatoka.common.capi.business.Comparators;
import de.hatoka.tournament.capi.entities.RankPO;

public final class RankPOComparators
{
    private RankPOComparators()
    {
    }

    public static final Comparator<RankPO> DEFAULT = new Comparator<RankPO>()
    {
        @Override
        public int compare(RankPO o1, RankPO o2)
        {
            // position
            return POSITION.compare(o1, o2);
        }
    };

    public static final Comparator<RankPO> POSITION = new Comparator<RankPO>()
    {
        @Override
        public int compare(RankPO o1, RankPO o2)
        {
            if (o1 == null || o2 == null)
            {
                return Comparators.NULL.compare(o1, o2);
            }
            int result = Comparators.INTEGER.compare(o1.getFirstPosition(), o2.getFirstPosition());
            if (result == 0)
            {
                result = Comparators.INTEGER.compare(o1.getLastPosition(), o2.getLastPosition());
            }
            return result;
        }
    };
}
