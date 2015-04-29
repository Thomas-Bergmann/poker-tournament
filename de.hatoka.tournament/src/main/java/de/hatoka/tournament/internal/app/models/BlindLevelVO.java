package de.hatoka.tournament.internal.app.models;

import java.util.Date;

public class BlindLevelVO
{
    private String id;
    private int smallBlind;
    private int bigBlind;
    private int ante;
    private boolean isPause = false;

    /**
     * Duration in minutes
     */
    private int duration;
    private Date estStartDateTime;
    private Date estEndDateTime;

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

    public String getId()
    {
        return id;
    }

    public void setId(String id)
    {
        this.id = id;
    }
}
