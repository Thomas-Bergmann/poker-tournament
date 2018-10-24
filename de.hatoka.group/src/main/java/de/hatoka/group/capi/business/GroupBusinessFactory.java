package de.hatoka.group.capi.business;

import de.hatoka.user.capi.business.UserRef;

public interface GroupBusinessFactory
{
    GroupBORepository getGroupBORepository(UserRef user);
}
