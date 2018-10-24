package de.hatoka.group.capi.business;

import de.hatoka.user.capi.business.UserBO;

public interface GroupAdminBO
{
    UserBO getUser();
    GroupBO getGroup();
    void remove();

}
