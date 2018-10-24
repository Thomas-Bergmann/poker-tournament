package de.hatoka.tournament.internal.business;

import java.util.Date;

import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import de.hatoka.tournament.capi.business.BlindLevelBO;
import de.hatoka.tournament.capi.business.PauseBO;
import de.hatoka.tournament.internal.persistence.BlindLevelPO;

@Component
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class PauseBOImpl implements PauseBO, IPauseBO
{
    private final BlindLevelPO blindLevelPO;
    private final ITournamentBO tournament;

    /**
     * Creates a blind level BO
     * @param blindLevelPO persistent object of blind level
     * @param dateProvider provides current date for activation of blind level
     */
    public PauseBOImpl(BlindLevelPO blindLevelPO, ITournamentBO tournament)
    {
        this.blindLevelPO = blindLevelPO;
        this.tournament = tournament;
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
        PauseBOImpl other = (PauseBOImpl)obj;
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
        tournament.start(this);
    }

    @Override
    public Date getStartTime()
    {
        return blindLevelPO.getStartDate();
    }

    @Override
    public boolean isActive()
    {
        return blindLevelPO.getPosition() == tournament.getCurrentRound();
    }

    @Override
    public BlindLevelBO getBlindLevel()
    {
        return null;
    }

    @Override
    public Date getEndTime()
    {
        return new Date(getStartTime().getTime() + getDuration() * 60_000);
    }

    @Override
    public Integer getPosition()
    {
        return blindLevelPO.getPosition();
    }

    @Override
    public void setStartDate(Date date)
    {
        blindLevelPO.setStartDate(date);
    }
}
