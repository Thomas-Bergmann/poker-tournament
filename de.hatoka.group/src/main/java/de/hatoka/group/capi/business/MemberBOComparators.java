package de.hatoka.group.capi.business;

import java.util.Comparator;

import de.hatoka.common.capi.math.Comparators;

public final class MemberBOComparators
{
    private MemberBOComparators()
    {
    }

    public static final Comparator<GroupMemberBO> BY_NAME = (o1, o2) -> {
        if (o1 == null || o2 == null)
        {
            return Comparators.NULL.compare(o1, o2);
        }
        return Comparators.STRING.compare(o1.getPlayer().get().getName(), o2.getPlayer().get().getName());
    };
}
