package de.hatoka.tournament.internal.app.models;

import java.time.Duration;
import java.util.Date;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import de.hatoka.common.capi.app.xslt.DurationXmlAdapter;
import de.hatoka.tournament.capi.business.BlindLevelBO;
import de.hatoka.tournament.capi.business.PauseBO;
import de.hatoka.tournament.capi.business.TournamentBO;

@XmlRootElement
public class TournamentBigScreenModel
{
    private BlindLevelVO currentBlindLevel;
    private BlindLevelVO nextBlindLevel;
    private int averageStackSize = 0;
    private int currentAmountPlayer = 0;
    private int maxAmountPlayers = 0;
    private Date currentTime;
    private Date nextPauseTime;

    @XmlAttribute
    @XmlJavaTypeAdapter(DurationXmlAdapter.class)
    private Duration duration;

    public TournamentBigScreenModel()
    {
    }

    public TournamentBigScreenModel(TournamentBO tournamentBO, Date currentTime)
    {
        this.currentAmountPlayer = tournamentBO.getActiveCompetitors().size();
        this.maxAmountPlayers = tournamentBO.getCompetitors().size();
        BlindLevelBO currentBlindLevelBO = tournamentBO.getCurrentBlindLevel();
        this.currentBlindLevel = currentBlindLevelBO == null ? null : new BlindLevelVO(currentBlindLevelBO);
        BlindLevelBO nextBlindLevelBO = tournamentBO.getNextBlindLevel();
        this.nextBlindLevel = nextBlindLevelBO == null ? null : new BlindLevelVO(nextBlindLevelBO);
        PauseBO pause = tournamentBO.getNextPause();
        this.nextPauseTime = pause == null ? null : pause.getStartTime();
        this.currentTime = currentTime;
        Date endTime = currentBlindLevelBO.getEndTime();
        this.duration = endTime == null ? null : Duration.ofMillis(endTime.getTime() - currentTime.getTime());
    }

    public BlindLevelVO getCurrentBlindLevel()
    {
        return currentBlindLevel;
    }

    public void setCurrentBlindLevel(BlindLevelVO currentBlindLevel)
    {
        this.currentBlindLevel = currentBlindLevel;
    }

    public BlindLevelVO getNextBlindLevel()
    {
        return nextBlindLevel;
    }

    public void setNextBlindLevel(BlindLevelVO nextBlindLevel)
    {
        this.nextBlindLevel = nextBlindLevel;
    }

    public int getAverageStackSize()
    {
        return averageStackSize;
    }

    public void setAverageStackSize(int averageStackSize)
    {
        this.averageStackSize = averageStackSize;
    }

    public int getCurrentAmountPlayer()
    {
        return currentAmountPlayer;
    }

    public void setCurrentAmountPlayer(int currentAmountPlayer)
    {
        this.currentAmountPlayer = currentAmountPlayer;
    }

    public int getMaxAmountPlayers()
    {
        return maxAmountPlayers;
    }

    public void setMaxAmountPlayers(int maxAmountPlayers)
    {
        this.maxAmountPlayers = maxAmountPlayers;
    }

    public Date getCurrentTime()
    {
        return currentTime;
    }

    public void setCurrentTime(Date currentTime)
    {
        this.currentTime = currentTime;
    }

    public Date getNextPauseTime()
    {
        return nextPauseTime;
    }

    public void setNextPauseTime(Date nextPauseTime)
    {
        this.nextPauseTime = nextPauseTime;
    }

    @XmlTransient
    public Duration getDuration()
    {
        return duration;
    }

    public void setDuration(Duration duration)
    {
        this.duration = duration;
    }
}
