package de.hatoka.group.internal.app.list;

import java.net.URI;

public class GroupListItemVO
{
    private String id;
    private String name;
    private int countMembers;
    private boolean isOwner = false;
    private URI uri;

    public GroupListItemVO()
    {
        // empty constructor
    }

    public GroupListItemVO(String id, String name, int countMembers, boolean isOwner)
    {
        this.id = id;
        this.name = name;
        this.countMembers = countMembers;
        this.setOwner(isOwner);
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

    public boolean isOwner()
    {
        return isOwner;
    }

    public void setOwner(boolean isOwner)
    {
        this.isOwner = isOwner;
    }

    public URI getUri()
    {
        return uri;
    }

    public void setUri(URI uri)
    {
        this.uri = uri;
    }

}
