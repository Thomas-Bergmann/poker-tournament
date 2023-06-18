package de.hatoka.group.capi.remote;

import java.util.Collections;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

public class GroupDataRO
{
    @JsonProperty("name")
    private String name;
    @JsonProperty("admins")
    private List<String> admins = Collections.emptyList();
    @JsonProperty("members")
    private List<String> members = Collections.emptyList();

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public List<String> getAdmins()
    {
        return admins;
    }

    public void setAdmins(List<String> admins)
    {
        this.admins = admins;
    }

    public List<String> getMembers()
    {
        return members;
    }

    public void setMembers(List<String> members)
    {
        this.members = members;
    }
}
