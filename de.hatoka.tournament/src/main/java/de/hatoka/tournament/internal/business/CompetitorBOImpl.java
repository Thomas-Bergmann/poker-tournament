package de.hatoka.tournament.internal.business;

import jakarta.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import de.hatoka.common.capi.configuration.DateProvider;
import de.hatoka.common.capi.math.Money;
import de.hatoka.common.capi.persistence.MoneyPOConverter;
import de.hatoka.player.capi.business.HistoryBORepository;
import de.hatoka.player.capi.business.PlayerBO;
import de.hatoka.player.capi.types.HistoryEntryType;
import de.hatoka.player.internal.business.PlayerBOFactory;
import de.hatoka.tournament.capi.business.TournamentBO;
import de.hatoka.tournament.capi.types.CompetitorState;
import de.hatoka.tournament.internal.persistence.CompetitorDao;
import de.hatoka.tournament.internal.persistence.CompetitorPO;

@Component("TournamentCompetitorBOImpl")
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class CompetitorBOImpl implements ICompetitorBO
{
    @Autowired
    private PlayerBOFactory playerFactory;
    @Autowired
    private CompetitorDao competitorDao;
    @Autowired
    private DateProvider dateProvider;
    @Autowired
    private HistoryBORepository historyRepository;

    private CompetitorPO competitorPO;
    private final TournamentBO gameBO;

    public CompetitorBOImpl(CompetitorPO competitorPO, TournamentBO gameBO)
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
    public void buyin()
    {
        Money amount = gameBO.getBuyIn();
        if (isActive())
        {
            throw new IllegalStateException("Buyin not allowed at active competitors");
        }
        competitorPO.setMoneyInvest(MoneyPOConverter.persistence(getInPlay().add(amount)));
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
        return MoneyPOConverter.valueOf(competitorPO.getMoneyInvest());
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
    public Money getResult()
    {
        return MoneyPOConverter.valueOf(competitorPO.getMoneyPayout());
    }

    @Transactional
    public void seatOpen(Money result, Integer position)
    {
        competitorPO.setState(CompetitorState.OUT);
        competitorPO.setMoneyPayout(MoneyPOConverter
                        .persistence(MoneyPOConverter.valueOf(competitorPO.getMoneyPayout())));
        competitorPO.setPosition(position);
        savePO();
        createEntry(HistoryEntryType.CashOut, result);
    }

    @Override
    public void rebuy()
    {
        Money amount = gameBO.getCurrentRebuy();
        if (!isActive())
        {
            throw new IllegalStateException("Rebuy not allowed at inactive competitors");
        }
        if (amount == null)
        {
            throw new IllegalStateException("Competitor can't rebuy at this blind level");
        }
        competitorPO.setMoneyInvest(MoneyPOConverter
                        .persistence(MoneyPOConverter.valueOf(competitorPO.getMoneyInvest()).add(amount)));
        savePO();
        sortCompetitors();
        createEntry(HistoryEntryType.ReBuy, amount);
    }

    private void createEntry(HistoryEntryType type, Money amount)
    {
        historyRepository.createEntry(dateProvider.get(), getPlayer().getRef(), gameBO.getRef().getGlobalRef(), type, amount);
    }

    @Override
    public void setPosition(Integer position)
    {
        competitorPO.setPosition(position);
        savePO();
    }

    @Override
    public CompetitorState getState()
    {
        return competitorPO.getState();
    }

    @Override
    public void setInactive()
    {
        competitorPO.setState(CompetitorState.OUT);
        competitorPO.setTableNo(-1);
        competitorPO.setSeatNo(-1);
        savePO();
    }

    @Override
    public void takeSeat(int tableNo, int seatNo)
    {
        competitorPO.setTableNo(tableNo);
        competitorPO.setSeatNo(seatNo);
        savePO();
    }

    @Override
    public Integer getTableNo()
    {
        return competitorPO.getTableNo() < 0 ? null : competitorPO.getTableNo();
    }

    @Override
    public Integer getSeatNo()
    {
        return competitorPO.getSeatNo() < 0 ? null : competitorPO.getSeatNo();
    }

    @Override
    public void remove()
    {
        competitorDao.delete(competitorPO);
        competitorPO = null;
    }

    private void savePO()
    {
        competitorPO = competitorDao.save(competitorPO);
    }
}
