package de.hatoka.tournament.internal.app.models;

import java.util.Date;

import de.hatoka.common.capi.business.Money;
import de.hatoka.tournament.capi.business.BlindLevelBO;
import de.hatoka.tournament.capi.business.TournamentRoundBO;

public class BlindLevelVO
{
    private String id = null;
    private int smallBlind = 0;
    private int bigBlind = 0;
    private int ante = 0;
    private boolean isPause = false;

    /**
     * Duration in minutes
     */
    private int duration;
    private Date estStartDateTime;
    private Date estEndDateTime;

    /**
     * rebuy at this level
     */
    private Money rebuy;

    public BlindLevelVO()
    {
    }

    public BlindLevelVO(TournamentRoundBO round)
    {
        BlindLevelBO blindLevelBO = round.getBlindLevelBO();
        id = round.getID();
        duration = round.getDuration();
        rebuy = round.getReBuy();
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

    public Money getRebuy()
    {
        return rebuy;
    }

    public void setRebuy(Money rebuy)
    {
        this.rebuy = rebuy;
    }
}
