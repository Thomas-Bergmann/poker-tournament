package de.hatoka.tournament.internal.business;

import de.hatoka.common.capi.business.Money;
import de.hatoka.tournament.capi.business.CompetitorBO;
import de.hatoka.tournament.capi.business.PlayerBO;
import de.hatoka.tournament.capi.business.TournamentBusinessFactory;
import de.hatoka.tournament.capi.entities.CompetitorPO;

public class CompetitorBOImpl implements CompetitorBO
{
    private CompetitorPO competitorPO;
    private final TournamentBusinessFactory factory;

    public CompetitorBOImpl(CompetitorPO competitorPO, TournamentBusinessFactory factory)
    {
        this.competitorPO = competitorPO;
        this.factory = factory;
    }

    @Override
    public int hashCode()
    {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((competitorPO == null) ? 0 : competitorPO.hashCode());
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
        CompetitorBOImpl other = (CompetitorBOImpl)obj;
        if (competitorPO == null)
        {
            if (other.competitorPO != null)
                return false;
        }
        else if (!competitorPO.equals(other.competitorPO))
            return false;
        return true;
    }

    @Override
    public void buyin(Money money)
    {
        if (isActive())
        {
            throw new IllegalStateException("Buyin not allowed at active competitors");
        }
        competitorPO.setMoneyInPlay(getInPlay().add(money).toMoneyPO());
        competitorPO.setActive(true);
    }

    @Override
    public String getID()
    {
        return competitorPO.getId();
    }

    @Override
    public Money getInPlay()
    {
        return Money.getInstance(competitorPO.getMoneyInPlay());
    }

    @Override
    public PlayerBO getPlayerBO()
    {
        return factory.getPlayerBO(competitorPO.getPlayerPO());
    }

    @Override
    public Integer getPosition()
    {
        return competitorPO.getPositition();
    }

    @Override
    public Money getResult()
    {
        return Money.getInstance(competitorPO.getMoneyResult());
    }

    @Override
    public boolean isActive()
    {
        return competitorPO.isActive();
    }

    @Override
    public void seatOpen(Money restAmount)
    {
        if (!isActive())
        {
            throw new IllegalStateException("seatOpen not allowed at inactive competitors");
        }
        if (restAmount != null)
        {
            // pay-back rest amount
            rebuy(restAmount.negate());
        }
        competitorPO.setActive(false);
        Money moneyResult = Money.getInstance(competitorPO.getMoneyInPlay()).negate();
        competitorPO.setMoneyResult(moneyResult.toMoneyPO());
    }

    @Override
    public void rebuy(Money money)
    {
        if (!isActive())
        {
            throw new IllegalStateException("Rebuy not allowed at inactive competitors");
        }
        competitorPO.setMoneyInPlay(Money.getInstance(competitorPO.getMoneyInPlay()).add(money).toMoneyPO());
    }

    @Override
    public void setPosition(Integer position)
    {
        competitorPO.setPositition(position);
    }

}
