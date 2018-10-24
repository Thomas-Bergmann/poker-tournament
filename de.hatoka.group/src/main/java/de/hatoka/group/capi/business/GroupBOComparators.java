package de.hatoka.group.capi.business;

import java.util.Comparator;

public final class GroupBOComparators
{
    private GroupBOComparators()
    {
    }

    public static final Comparator<GroupBO> BY_NAME = (a, b) -> a.getName().compareTo(b.getName());
}
