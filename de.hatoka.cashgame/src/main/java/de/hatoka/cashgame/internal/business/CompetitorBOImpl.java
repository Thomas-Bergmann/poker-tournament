package de.hatoka.cashgame.internal.business;

import jakarta.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import de.hatoka.cashgame.capi.business.CashGameBO;
import de.hatoka.cashgame.capi.types.CompetitorState;
import de.hatoka.cashgame.internal.persistence.CashGameCompetitorDao;
import de.hatoka.cashgame.internal.persistence.CashGameCompetitorPO;
import de.hatoka.common.capi.configuration.DateProvider;
import de.hatoka.common.capi.math.Money;
import de.hatoka.common.capi.persistence.MoneyPOConverter;
import de.hatoka.player.capi.business.HistoryBORepository;
import de.hatoka.player.capi.business.PlayerBO;
import de.hatoka.player.capi.types.HistoryEntryType;
import de.hatoka.player.internal.business.PlayerBOFactory;

@Component("CashGameCompetitorBOImpl")
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class CompetitorBOImpl implements ICompetitorBO
{
    @Autowired
    private PlayerBOFactory playerFactory;
    @Autowired
    private CashGameCompetitorDao competitorDao;
    @Autowired
    private HistoryBORepository historyRepository;
    @Autowired
    private DateProvider dateProvider;

    private CashGameCompetitorPO competitorPO;
    private final CashGameBO gameBO;

    public CompetitorBOImpl(CashGameCompetitorPO competitorPO, CashGameBO gameBO)
    {
        this.competitorPO = competitorPO;
        this.gameBO = gameBO;
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
        competitorPO.setMoneyInvest(MoneyPOConverter.persistence(MoneyPOConverter.valueOf(competitorPO.getMoneyInvest()).add(amount)));
        competitorPO.setState(CompetitorState.ACTIVE);
        savePO();
        sortCompetitors();
        createEntry(HistoryEntryType.BuyIn, amount);
    }

    private void sortCompetitors()
    {
        gameBO.sortCompetitors();
    }

    @Override
    public Money getInPlay()
    {
        Money invest = MoneyPOConverter.valueOf(competitorPO.getMoneyInvest());
        Money payout = MoneyPOConverter.valueOf(competitorPO.getMoneyPayout());
        return invest.subtract(payout);
    }

    @Override
    public PlayerBO getPlayer()
    {
        return playerFactory.get(competitorPO.getPlayerRef()).get();
    }

    @Override
    public Integer getPosition()
    {
        return competitorPO.getPosition();
    }

    @Override
    public void rebuy(Money amount)
    {
        if (!isActive())
        {
            throw new IllegalStateException("Rebuy not allowed at inactive competitors");
        }
        if (amount == null)
        {
            throw new IllegalStateException("Competitor can't rebuy at this blind level");
        }
        competitorPO.setMoneyInvest(MoneyPOConverter.persistence(MoneyPOConverter.valueOf(competitorPO.getMoneyInvest()).add(amount)));
        savePO();
        sortCompetitors();
        createEntry(HistoryEntryType.ReBuy, amount);
    }

    @Override
    public void setPosition(Integer position)
    {
        competitorPO.setPosition(position);
        savePO();
    }

    @Override
    @Transactional
    public Money payout(Money amount)
    {
        competitorPO.setMoneyPayout(MoneyPOConverter.persistence(MoneyPOConverter.valueOf(competitorPO.getMoneyPayout()).add(amount)));
        competitorPO.setState(CompetitorState.OUT);
        savePO();
        Money result = getResult();
        createEntry(HistoryEntryType.CashOut, result);
        return result;
    }

    @Override
    public CompetitorState getState()
    {
        return competitorPO.getState();
    }

    @Override
    public void standUp()
    {
        competitorPO.setState(CompetitorState.INACTIVE);
        savePO();
    }

    @Override
    public void takeSeat()
    {
        competitorPO.setState(CompetitorState.ACTIVE);
        savePO();
    }

    @Override
    public void remove()
    {
        competitorDao.delete(competitorPO);
        competitorPO = null;
    }

    private void createEntry(HistoryEntryType type, Money amount)
    {
        historyRepository.createEntry(dateProvider.get(), getPlayer().getRef(), gameBO.getRef().getGlobalRef(), type, amount);
    }

    private void savePO()
    {
        competitorPO = competitorDao.save(competitorPO);
    }
}
