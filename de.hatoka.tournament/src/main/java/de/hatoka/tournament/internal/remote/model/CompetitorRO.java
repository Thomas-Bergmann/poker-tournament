package de.hatoka.tournament.internal.remote.model;

import de.hatoka.common.capi.math.Money;
import de.hatoka.tournament.capi.types.CompetitorState;

public class CompetitorRO
{
    private String playerRef;
    private String playerName;
    private CompetitorState status;

    private Money inPlay;
    private Money result;

    private boolean active = false;
    private Integer position;

    private Integer tableNo;
    private Integer seatNo;

    public CompetitorRO()
    {
    }

    public Money getInPlay()
    {
        return inPlay;
    }

    public String getPlayerRef()
    {
        return playerRef;
    }

    public String getPlayerName()
    {
        return playerName;
    }

    public Integer getPosition()
    {
        return position;
    }

    public Money getResult()
    {
        return result;
    }

    public boolean isActive()
    {
        return active;
    }

    public void setActive(boolean active)
    {
        this.active = active;
    }

    public void setInPlay(Money inPlay)
    {
        this.inPlay = inPlay;
    }

    public void setPlayerRef(String playerRef)
    {
        this.playerRef = playerRef;
    }

    public void setPlayerName(String playerName)
    {
        this.playerName = playerName;
    }

    public void setPosition(Integer position)
    {
        this.position = position;
    }

    public void setResult(Money result)
    {
        this.result = result;
    }

    public Integer getTableNo()
    {
        return tableNo;
    }

    public void setTableNo(Integer tableNo)
    {
        this.tableNo = tableNo;
    }

    public Integer getSeatNo()
    {
        return seatNo;
    }

    public void setSeatNo(Integer seatNo)
    {
        this.seatNo = seatNo;
    }

    public CompetitorState getStatus()
    {
        return status;
    }

    public void setStatus(CompetitorState status)
    {
        this.status = status;
    }

}
