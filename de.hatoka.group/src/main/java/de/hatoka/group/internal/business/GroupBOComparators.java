package de.hatoka.group.internal.business;

import java.util.Comparator;

import de.hatoka.group.capi.business.GroupBO;

public final class GroupBOComparators
{
    private GroupBOComparators()
    {
    }

    public static final Comparator<GroupBO> DEFAULT_GROUP= (a, b) -> a.getName().compareTo(b.getName());
}
