package de.hatoka.tournament.internal.app.models;

import de.hatoka.tournament.capi.business.PlayerBO;

public class PlayerVO
{
    private String id;
    private String name;

    public PlayerVO()
    {
    }

    public PlayerVO(PlayerBO player)
    {
        setId(player.getID());
        setName(player.getName());
    }

    public String getId()
    {
        return id;
    }

    public String getName()
    {
        return name;
    }

    public void setId(String id)
    {
        this.id = id;
    }

    public void setName(String name)
    {
        this.name = name;
    }

}
