package de.hatoka.group.internal.app.member;

import java.util.List;
import java.util.stream.Collectors;

import javax.inject.Inject;

import de.hatoka.common.capi.app.servlet.RequestUserRefProvider;
import de.hatoka.group.capi.business.GroupBO;
import de.hatoka.group.capi.business.GroupBusinessFactory;
import de.hatoka.group.capi.business.MemberBO;

public class GroupMemberAction
{
    @Inject
    private GroupBusinessFactory factory;
    @Inject
    private RequestUserRefProvider userRefProvider;

    private final String groupID;

    public GroupMemberAction(String groupID)
    {
        this.groupID = groupID;
    }

    public GroupMemberModel getGroupModel()
    {
        return new GroupMemberModel(groupID, getAccessableGroup().getName());
    }

    private GroupBO getAccessableGroup()
    {
        GroupBO groupBO = factory.getGroupBOByID(groupID);
        String userRef = userRefProvider.getUserRef();
        if (!groupBO.isMember(userRef))
        {
            throw new IllegalAccessError("User '" + userRef + "' can't access group '" + groupID + "'.");
        }
        return groupBO;
    }

    public List<GroupMemberRO> getMembers()
    {
        GroupBO groupBO = getAccessableGroup();
        return groupBO.getMembers().stream().map(bo -> getGroupMemberRO(bo)).collect(Collectors.toList());
    }

    private static GroupMemberRO getGroupMemberRO(MemberBO bo)
    {
        GroupMemberRO ro = new GroupMemberRO();
        ro.setId(bo.getID());
        ro.setName(bo.getName());
        ro.setOwner(bo.isOwner());
        return ro;
    }
}
