package de.hatoka.tournament.internal.app.models;

import java.util.Date;

import de.hatoka.tournament.capi.business.BlindLevelBO;
import de.hatoka.tournament.capi.business.TournamentRoundBO;

public class BlindLevelVO
{
    private String id = null;
    private int smallBlind = 0;
    private int bigBlind = 0;
    private int ante = 0;
    private boolean isPause = false;
    private boolean isRebuy = false;

    /**
     * Duration in minutes
     */
    private int duration;
    private Date estStartDateTime;
    private Date estEndDateTime;

    public BlindLevelVO()
    {
    }

    public BlindLevelVO(TournamentRoundBO round)
    {
        BlindLevelBO blindLevelBO = round.getBlindLevel();
        id = round.getID();
        duration = round.getDuration();
        setRebuy(round.isRebuyAllowed());
        if (blindLevelBO == null)
        {
            isPause = true;
        }
        else
        {
            smallBlind = blindLevelBO.getSmallBlind();
            bigBlind = blindLevelBO.getBigBlind();
            ante = blindLevelBO.getAnte();
        }
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

    public String getId()
    {
        return id;
    }

    public void setId(String id)
    {
        this.id = id;
    }

    public boolean isRebuy()
    {
        return isRebuy;
    }

    public void setRebuy(boolean isRebuy)
    {
        this.isRebuy = isRebuy;
    }
}
