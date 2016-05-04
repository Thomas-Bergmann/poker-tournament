package de.hatoka.group.capi.business;

import java.util.List;

public interface GroupBORepository
{
    GroupBO createGroup(String name, String ownerName);

    List<GroupBO> getGroups();
}
