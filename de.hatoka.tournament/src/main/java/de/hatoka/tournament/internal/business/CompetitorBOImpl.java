package de.hatoka.tournament.internal.business;

import java.util.Date;

import com.google.inject.Provider;

import de.hatoka.common.capi.business.Money;
import de.hatoka.tournament.capi.business.GameBO;
import de.hatoka.tournament.capi.business.PlayerBO;
import de.hatoka.tournament.capi.business.TournamentBusinessFactory;
import de.hatoka.tournament.capi.dao.HistoryDao;
import de.hatoka.tournament.capi.entities.CompetitorPO;
import de.hatoka.tournament.capi.entities.HistoryPO;
import de.hatoka.tournament.capi.types.CompetitorState;
import de.hatoka.tournament.capi.types.HistoryEntryType;

public class CompetitorBOImpl implements ICompetitor
{
    private CompetitorPO competitorPO;
    private final TournamentBusinessFactory factory;
    private final GameBO gameBO;
    private final HistoryDao historyDao;
    private final Provider<Date> dateProvider;

    public CompetitorBOImpl(CompetitorPO competitorPO, GameBO gameBO, HistoryDao historyDao,Provider<Date> dateProvider,
                    TournamentBusinessFactory factory)
    {
        this.competitorPO = competitorPO;
        this.gameBO = gameBO;
        this.historyDao = historyDao;
        this.dateProvider = dateProvider;
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
    public void buyin(Money amount)
    {
        if (isActive())
        {
            throw new IllegalStateException("Buyin not allowed at active competitors");
        }
        competitorPO.setMoneyInPlay(getInPlay().add(amount).toMoneyPO());
        competitorPO.setState(CompetitorState.ACTIVE.name());
        sortCompetitors();
        createEntry(HistoryEntryType.BuyIn, amount);
    }

    private void sortCompetitors()
    {
        gameBO.sortCompetitors();
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
    public PlayerBO getPlayer()
    {
        return factory.getPlayerBO(competitorPO.getPlayerPO());
    }

    @Override
    public Integer getPosition()
    {
        return competitorPO.getPosition();
    }

    @Override
    public Money getResult()
    {
        return Money.getInstance(competitorPO.getMoneyResult());
    }

    @Override
    public void rebuy(Money amount)
    {
        if (!isActive())
        {
            throw new IllegalStateException("Rebuy not allowed at inactive competitors");
        }
        competitorPO.setMoneyInPlay(Money.getInstance(competitorPO.getMoneyInPlay()).add(amount).toMoneyPO());
        sortCompetitors();
        createEntry(HistoryEntryType.ReBuy, amount);
    }

    @Override
    public void setPosition(Integer position)
    {
        competitorPO.setPosition(position);
    }

    @Override
    public void createEntry(HistoryEntryType type, Money amount)
    {
        HistoryPO entry = historyDao.createAndInsert(competitorPO.getTournamentPO(), competitorPO.getPlayerPO(), dateProvider.get());
        entry.setActionKey(type.name());
        entry.setAmount(amount.toMoneyPO());
    }

    @Override
    public void setInPlay(Money amount)
    {
        competitorPO.setMoneyInPlay(amount.toMoneyPO());
    }

    @Override
    public void setResult(Money amount)
    {
        competitorPO.setMoneyResult(amount.toMoneyPO());
    }

    @Override
    public CompetitorState getState()
    {
        return CompetitorState.valueOf(competitorPO.getState());
    }

    @Override
    public void setInactive()
    {
        competitorPO.setState(CompetitorState.INACTIVE.name());
    }
}
