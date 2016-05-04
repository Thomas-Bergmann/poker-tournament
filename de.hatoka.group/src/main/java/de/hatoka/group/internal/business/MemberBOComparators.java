package de.hatoka.group.internal.business;

import java.util.Comparator;

import de.hatoka.common.capi.business.Comparators;
import de.hatoka.group.capi.business.MemberBO;

public final class MemberBOComparators
{
    private MemberBOComparators()
    {
    }

    public static final Comparator<MemberBO> NAME = (o1, o2) -> {
        if (o1 == null || o2 == null)
        {
            return Comparators.NULL.compare(o1, o2);
        }
        return Comparators.STRING.compare(o1.getName(), o2.getName());
    };
}
