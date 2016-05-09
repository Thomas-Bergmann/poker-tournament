package de.hatoka.group.internal.app.servlets;

import java.util.List;

import javax.inject.Inject;

import de.hatoka.common.capi.app.servlet.RequestUserRefProvider;
import de.hatoka.group.capi.business.GroupBO;
import de.hatoka.group.capi.business.GroupBORepository;
import de.hatoka.group.capi.business.GroupBusinessFactory;
import de.hatoka.group.internal.app.models.GroupListItemVO;
import de.hatoka.group.internal.app.models.GroupListModel;

public class GroupListAction
{
    @Inject
    private RequestUserRefProvider userRefProvider;
    @Inject
    private GroupBusinessFactory factory;

    public GroupListModel getGroupListModel()
    {
        GroupListModel result = new GroupListModel();
        GroupBORepository groupBORepository = factory.getGroupBORepository(userRefProvider.getUserRef());
        for(GroupBO group : groupBORepository.getGroups())
        {
            result.getGroups().add(new GroupListItemVO(group.getID(), group.getName(), group.getMembers().size()));
        }
        return result;
    }

    public void delete(List<String> identifiers)
    {
    }

    public void create(String name)
    {
    }

}
