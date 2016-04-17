package de.hatoka.tournament.internal.business;

import java.util.Date;

import javax.inject.Provider;

import de.hatoka.tournament.capi.business.BlindLevelBO;
import de.hatoka.tournament.capi.entities.BlindLevelPO;

public class BlindLevelBOImpl implements BlindLevelBO
{
    private final BlindLevelPO blindLevelPO;
    private final ITournamentBO tournament;
    private final Provider<Date> dateProvider;

    /**
     * Creates a blind level BO
     * @param blindLevelPO persistent object of blind level
     * @param dateProvider provides current date for activation of blind level
     */
    public BlindLevelBOImpl(BlindLevelPO blindLevelPO, ITournamentBO tournament, Provider<Date> dateProvider)
    {
        this.blindLevelPO = blindLevelPO;
        this.tournament = tournament;
        this.dateProvider = dateProvider;
    }

    @Override
    public int hashCode()
    {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((blindLevelPO == null) ? 0 : blindLevelPO.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj)
    {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        BlindLevelBOImpl other = (BlindLevelBOImpl)obj;
        if (blindLevelPO == null)
        {
            if (other.blindLevelPO != null)
                return false;
        }
        else if (!blindLevelPO.equals(other.blindLevelPO))
            return false;
        return true;
    }

    @Override
    public Integer getDuration()
    {
        return blindLevelPO.getDuration();
    }

    @Override
    public Integer getSmallBlind()
    {
        return blindLevelPO.getSmallBlind();
    }

    @Override
    public Integer getBigBlind()
    {
        return blindLevelPO.getBigBlind();
    }

    @Override
    public Integer getAnte()
    {
        return blindLevelPO.getAnte();
    }

    @Override
    public String getID()
    {
        return blindLevelPO.getId();
    }

    @Override
    public BlindLevelBO getBlindLevel()
    {
        if (blindLevelPO.isPause())
        {
            return null;
        }
        return this;
    }

    @Override
    public boolean isRebuyAllowed()
    {
        return blindLevelPO.isReBuy();
    }

    @Override
    public void allowRebuy(boolean allow)
    {
        blindLevelPO.setReBuy(allow);
    }

    @Override
    public void start()
    {
        blindLevelPO.getTournamentPO().setCurrentRound(blindLevelPO.getPosition());
        blindLevelPO.setStartDate(dateProvider.get());
        tournament.defineBlindLevelStartTimes();
    }

    @Override
    public Date getStartTime()
    {
        return blindLevelPO.getStartDate();
    }

    @Override
    public boolean isActive()
    {
        return blindLevelPO.getPosition() == blindLevelPO.getTournamentPO().getCurrentRound();
    }

    @Override
    public Date getEndTime()
    {
        return new Date(getStartTime().getTime() + getDuration() * 60_000);
    }
}
