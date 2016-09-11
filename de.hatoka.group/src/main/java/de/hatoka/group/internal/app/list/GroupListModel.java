package de.hatoka.group.internal.app.list;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class GroupListModel
{
    private List<GroupListItemVO> groups = new ArrayList<>();

    public List<GroupListItemVO> getGroups()
    {
        return groups;
    }

    public void setGroups(List<GroupListItemVO> groups)
    {
        this.groups = groups;
    }

}
