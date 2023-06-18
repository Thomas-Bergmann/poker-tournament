package de.hatoka.group.capi.remote;

import com.fasterxml.jackson.annotation.JsonProperty;

public class GroupInfoRO
{
    @JsonProperty("countMember")
    private int countMember = 0;
    @JsonProperty("countAdmin")
    private int countAdmin = 0;

    public int getCountMember()
    {
        return countMember;
    }

    public void setCountMember(int countMember)
    {
        this.countMember = countMember;
    }

    public int getCountAdmin()
    {
        return countAdmin;
    }

    public void setCountAdmin(int countAdmin)
    {
        this.countAdmin = countAdmin;
    }
}
