package de.hatoka.group.internal.app.member;

public class GroupMemberRO
{
    private String id;
    private String name;
    private boolean isOwner;
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
    public boolean isOwner()
    {
        return isOwner;
    }
    public void setOwner(boolean isOwner)
    {
        this.isOwner = isOwner;
    }
}
