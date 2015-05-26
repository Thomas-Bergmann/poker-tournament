package de.hatoka.tournament.internal.business;

import java.math.BigDecimal;
import java.util.Currency;

import de.hatoka.common.capi.business.Money;
import de.hatoka.tournament.capi.business.RankBO;
import de.hatoka.tournament.capi.business.TournamentBO;
import de.hatoka.tournament.capi.entities.RankPO;

public class RankBOImpl implements RankBO
{
    private final RankPO rankPO;
    private final TournamentBO tournamentBO;

    public RankBOImpl(RankPO rankPO, TournamentBO tournamentBO)
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
    public String getID()
    {
        return rankPO.getId();
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
        return rankPO.getPercentage();
    }

    @Override
    public Money getAmountPerPlayer()
    {
        Money sum = getAmount();
        long numberOfPlayers = getLastPosition() - getFirstPosition() + 1;
        return sum.divide(numberOfPlayers);
    }

    @Override
    public Money getAmount()
    {
        BigDecimal amount = rankPO.getAmount();
        if (amount != null)
        {
            return Money.valueOf(amount, getCurrency());
        }
        BigDecimal percentage = rankPO.getPercentage();
        if (percentage == null)
        {
            percentage = getCalculatedPercentage();
        }
        Money sum = tournamentBO.getSumInplay();
        Money fix = getFixRankAmount();
        return sum.subtract(fix).multiply(percentage).stripTrailingZeros();
    }

    private BigDecimal getCalculatedPercentage()
    {
        BigDecimal result = BigDecimal.ONE;
        long number = 0;
        for(RankPO rank : rankPO.getTournament().getRanks())
        {
            BigDecimal perc = rank.getPercentage();
            if (perc != null)
            {
                result = result.subtract(perc);
            }
            else
            {
                if (rank.getAmount() == null)
                {
                    number++;
                }
            }
        }
        if (number == 0 || result.signum() != 1)
        {
            return BigDecimal.ZERO;
        }
        return result.divide(BigDecimal.valueOf(number)).stripTrailingZeros();
    }

    private Currency getCurrency()
    {
        return tournamentBO.getBuyIn().getCurrency();
    }

    private Money getFixRankAmount()
    {
        BigDecimal result = BigDecimal.ZERO;
        for(RankPO rank : rankPO.getTournament().getRanks())
        {
            BigDecimal amount = rank.getAmount();
            if (amount != null)
            {
                result = result.add(amount);
            }
        }
        return Money.valueOf(result, getCurrency());
    }

}
