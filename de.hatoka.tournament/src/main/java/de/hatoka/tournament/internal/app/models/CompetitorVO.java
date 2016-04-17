package de.hatoka.tournament.internal.app.models;

import java.util.ArrayList;
import java.util.List;

import de.hatoka.common.capi.app.model.ActionVO;
import de.hatoka.common.capi.app.model.MoneyVO;
import de.hatoka.tournament.capi.business.CompetitorBO;

public class CompetitorVO
{
    private String id;
    private String playerId;
    private String playerName;
    private String status;

    private MoneyVO inPlay;
    private MoneyVO result;

    private boolean active = false;
    private Integer position;
    private List<ActionVO> actions = new ArrayList<>();

    private Integer tableNo;
    private Integer seatNo;

    public CompetitorVO()
    {
    }

    public CompetitorVO(CompetitorBO competitor)
    {
        id = competitor.getID();
        inPlay = competitor.getInPlay() == null ? null : new MoneyVO(competitor.getInPlay());
        result= competitor.getResult() == null ? null : new MoneyVO(competitor.getResult());
        position = competitor.getPosition();
        playerId = competitor.getPlayer().getID();
        playerName = competitor.getPlayer().getName();
        active = competitor.isActive();
        tableNo = competitor.getTableNo() == null ? null : competitor.getTableNo() + 1;
        seatNo = competitor.getSeatNo() == null ? null : competitor.getSeatNo() + 1;
        status = competitor.getState().name();
    }

    public String getId()
    {
        return id;
    }

    public MoneyVO getInPlay()
    {
        return inPlay;
    }

    public String getPlayerId()
    {
        return playerId;
    }

    public String getPlayerName()
    {
        return playerName;
    }

    public Integer getPosition()
    {
        return position;
    }

    public MoneyVO getResult()
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

    public void setId(String id)
    {
        this.id = id;
    }

    public void setInPlay(MoneyVO inPlay)
    {
        this.inPlay = inPlay;
    }

    public void setPlayerId(String playerId)
    {
        this.playerId = playerId;
    }

    public void setPlayerName(String playerName)
    {
        this.playerName = playerName;
    }

    public void setPosition(Integer position)
    {
        this.position = position;
    }

    public void setResult(MoneyVO result)
    {
        this.result = result;
    }

    public List<ActionVO> getActions()
    {
        return actions;
    }

    public void setActions(List<ActionVO> actions)
    {
        this.actions = actions;
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

    public String getStatus()
    {
        return status;
    }

    public void setStatus(String status)
    {
        this.status = status;
    }

}
