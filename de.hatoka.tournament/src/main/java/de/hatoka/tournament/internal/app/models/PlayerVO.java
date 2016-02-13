package de.hatoka.tournament.internal.app.models;

import de.hatoka.tournament.capi.business.PlayerBO;

public class PlayerVO
{
    private String id;
    private String name;
    private String eMail;

    public PlayerVO()
    {
    }

    public PlayerVO(PlayerBO player)
    {
        setId(player.getID());
        setName(player.getName());
        eMail = player.getEMail();
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

    public String geteMail()
    {
        return eMail;
    }

    public void seteMail(String eMail)
    {
        this.eMail = eMail;
    }

}
