package de.hatoka.tournament.internal.app.models;

import de.hatoka.tournament.capi.business.PlayerBO;

public class PlayerVO
{
    private String id;
    private String name;
    private String eMail;
    private int countTournaments = 0;
    private int countCashGames = 0;

    public PlayerVO()
    {
    }

    public PlayerVO(PlayerBO player)
    {
        setId(player.getID());
        setName(player.getName());
        eMail = player.getEMail();
        countTournaments = player.getTournaments().size();
        countCashGames = player.getCashGames().size();
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

    public int getCountTournaments()
    {
        return countTournaments;
    }

    public void setCountTournaments(int countTournaments)
    {
        this.countTournaments = countTournaments;
    }

    public int getCountCashGames()
    {
        return countCashGames;
    }

    public void setCountCashGames(int countCashGames)
    {
        this.countCashGames = countCashGames;
    }

}
