package de.hatoka.tournament.internal.business;

import java.math.BigDecimal;
import java.util.Currency;

import de.hatoka.common.capi.business.Money;
import de.hatoka.tournament.capi.business.BlindLevelBO;
import de.hatoka.tournament.capi.entities.BlindLevelPO;

public class BlindLevelBOImpl implements BlindLevelBO
{
    private final BlindLevelPO blindLevelPO;
    private final Currency currency;

    public BlindLevelBOImpl(BlindLevelPO blindLevelPO, Currency currency)
    {
        this.blindLevelPO = blindLevelPO;
        this.currency = currency;
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
    public Money getReBuy()
    {
        final BigDecimal rebuyAmount = blindLevelPO.getRebuyAmount();
        if (rebuyAmount == null)
        {
            return null;
        }
        return Money.valueOf(rebuyAmount, currency);
    }

}
