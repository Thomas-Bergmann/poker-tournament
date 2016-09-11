package de.hatoka.group.internal.app.member;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class GroupMemberModel
{
    private String id;
    private String name;

    public GroupMemberModel()
    {
    }

    public GroupMemberModel(String groupID, String name)
    {
        this.name = name;
        this.id = groupID;
    }

    public String getId()
    {
        return id;
    }

    public void setId(String id)
    {
        this.id = id;
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }
}
