package de.hatoka.group.internal.app.models;

public class GroupListItemVO
{
    private String id;
    private String name;
    private int countMembers;

    public GroupListItemVO()
    {
        // empty constructor
    }

    public GroupListItemVO(String id, String name, int countMembers)
    {
        this.id = id;
        this.name = name;
        this.countMembers = countMembers;
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

    public int getCountMembers()
    {
        return countMembers;
    }

    public void setCountMembers(int countMembers)
    {
        this.countMembers = countMembers;
    }

}
