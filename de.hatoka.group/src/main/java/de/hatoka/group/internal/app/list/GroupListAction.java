package de.hatoka.group.internal.app.list;

import java.util.List;

import javax.inject.Inject;
import javax.ws.rs.core.UriBuilder;

import de.hatoka.common.capi.app.servlet.RequestUserRefProvider;
import de.hatoka.group.capi.business.GroupBO;
import de.hatoka.group.capi.business.GroupBORepository;
import de.hatoka.group.capi.business.GroupBusinessFactory;

public class GroupListAction
{
    @Inject
    private RequestUserRefProvider userRefProvider;
    @Inject
    private GroupBusinessFactory factory;

    public GroupListModel getGroupListModel(UriBuilder uriBuilder)
    {
        GroupListModel result = new GroupListModel();
        String userRef = userRefProvider.getUserRef();
        for(GroupBO group : factory.getGroupBOsByUser(userRef))
        {
            GroupListItemVO item = new GroupListItemVO(group.getID(), group.getName(), group.getMembers().size(), group.getOwner().equals(userRef));
            item.setUri(uriBuilder.build(item.getId()));
            result.getGroups().add(item);
        }
        return result;
    }

    public void delete(List<String> identifiers)
    {
        String userRef = userRefProvider.getUserRef();
        for(GroupBO group : factory.getGroupBOsByUser(userRef))
        {
            if (identifiers.contains(group.getID()))
            {
                if (group.getOwner().equals(userRef))
                {
                    group.remove();
                }
                else
                {
                    group.getMember(userRef).remove();
                }
            }
        }
    }

    /**
     * Creates a new group
     * @param name name of group
     * @param ownerName member name of owner
     */
    public void create(String name, String ownerName)
    {
        GroupBORepository repository = factory.getGroupBORepository(userRefProvider.getUserRef());
        repository.createGroup(name, ownerName);
    }

    /**
     *
     * @param name
     * @param memberName
     */
    public void join(String name, String memberName)
    {
        GroupBO groupBO = factory.findGroupBOByName(name);
        if (groupBO == null)
        {
            // TODO group not found error message
            return;
        }
        if(!groupBO.isMember(userRefProvider.getUserRef()))
        {
            groupBO.createMember(userRefProvider.getUserRef(), memberName);
        }
    }

    public GroupListModel getGroupAddModel()
    {
        return new GroupListModel();
    }

}
