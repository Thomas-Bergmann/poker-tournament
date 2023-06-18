package de.hatoka.tournament.internal.remote.model;

import java.util.Date;

public class BlindLevelRO
{
    private int position = 0;
    private int smallBlind = 0;
    private int bigBlind = 0;
    private int ante = 0;
    private boolean isPause = false;
    private boolean isRebuy = false;
    private boolean isActive = false;

    /**
     * Duration in minutes
     */
    private int duration;
    private Date estStartDateTime;
    private Date estEndDateTime;

    public BlindLevelRO()
    {
    }

    public int getSmallBlind()
    {
        return smallBlind;
    }

    public void setSmallBlind(int smallBlind)
    {
        this.smallBlind = smallBlind;
    }

    public int getBigBlind()
    {
        return bigBlind;
    }

    public void setBigBlind(int bigBlind)
    {
        this.bigBlind = bigBlind;
    }

    public int getAnte()
    {
        return ante;
    }

    public void setAnte(int antBlind)
    {
        this.ante = antBlind;
    }

    public boolean isPause()
    {
        return isPause;
    }

    public void setPause(boolean isPause)
    {
        this.isPause = isPause;
    }

    public Date getEstStartDateTime()
    {
        return estStartDateTime;
    }

    public void setEstStartDateTime(Date estStartDateTime)
    {
        this.estStartDateTime = estStartDateTime;
    }

    public Date getEstEndDateTime()
    {
        return estEndDateTime;
    }

    public void setEstEndDateTime(Date estEndDateTime)
    {
        this.estEndDateTime = estEndDateTime;
    }

    public int getDuration()
    {
        return duration;
    }

    public void setDuration(int duration)
    {
        this.duration = duration;
    }

    public boolean isRebuy()
    {
        return isRebuy;
    }

    public void setRebuy(boolean isRebuy)
    {
        this.isRebuy = isRebuy;
    }

    public boolean isActive()
    {
        return isActive;
    }

    public void setActive(boolean isActive)
    {
        this.isActive = isActive;
    }

    public int getPosition()
    {
        return position;
    }

    public void setPosition(int position)
    {
        this.position = position;
    }
}
