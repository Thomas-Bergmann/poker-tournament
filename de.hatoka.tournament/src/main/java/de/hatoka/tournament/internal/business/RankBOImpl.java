package de.hatoka.tournament.internal.business;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Currency;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import de.hatoka.common.capi.math.Money;
import de.hatoka.tournament.internal.persistence.RankDao;
import de.hatoka.tournament.internal.persistence.RankPO;

@Component
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class RankBOImpl implements IRankBO
{
    @Autowired
    private RankDao rankDao;

    private RankPO rankPO;
    private final ITournamentBO tournamentBO;

    public RankBOImpl(RankPO rankPO, ITournamentBO tournamentBO)
    {
        this.rankPO = rankPO;
        this.tournamentBO = tournamentBO;
    }

    @Override
    public int hashCode()
    {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((rankPO == null) ? 0 : rankPO.hashCode());
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
        RankBOImpl other = (RankBOImpl)obj;
        if (rankPO == null)
        {
            if (other.rankPO != null)
                return false;
        }
        else if (!rankPO.equals(other.rankPO))
            return false;
        return true;
    }

    @Override
    public Integer getFirstPosition()
    {
        return rankPO.getFirstPosition();
    }

    @Override
    public Integer getLastPosition()
    {
        return rankPO.getLastPosition();
    }

    @Override
    public BigDecimal getPercentage()
    {
        BigDecimal percentage = rankPO.getPercentage();
        if (percentage == null)
        {
            return null;
        }
        return percentage.setScale(2, RoundingMode.HALF_EVEN);
    }

    @Override
    public Money getAmountPerPlayer()
    {
        Money sum = getAmount();
        long numberOfPlayers = getLastPosition() - getFirstPosition() + 1;
        // it must be DOWN otherwise the sum of the rank is higher than the available amount
        return sum.divide(numberOfPlayers).round(RoundingMode.DOWN);
    }

    @Override
    public Money getAmount()
    {
        BigDecimal amount = rankPO.getAmount();
        if (amount != null)
        {
            return Money.valueOf(amount.stripTrailingZeros(), getCurrency());
        }
        return null;
    }

    private Currency getCurrency()
    {
        return tournamentBO.getBuyIn().getCurrency();
    }

    @Override
    public void remove()
    {
        rankDao.delete(rankPO);
        rankPO = null;
        tournamentBO.removeRank(this);
    }

    @Override
    public void setAmount(Money amount)
    {
        rankPO.setAmount(amount.getAmount());
        savePO();
    }

    private void savePO()
    {
        rankPO = rankDao.save(rankPO);
    }
}
