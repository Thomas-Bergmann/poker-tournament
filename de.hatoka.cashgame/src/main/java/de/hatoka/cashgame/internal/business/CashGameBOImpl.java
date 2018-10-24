package de.hatoka.cashgame.internal.business;

import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import de.hatoka.cashgame.capi.business.CashGameBO;
import de.hatoka.cashgame.capi.business.CashGameRef;
import de.hatoka.cashgame.capi.business.CompetitorBO;
import de.hatoka.cashgame.capi.types.CompetitorState;
import de.hatoka.cashgame.internal.persistence.CashGameCompetitorDao;
import de.hatoka.cashgame.internal.persistence.CashGameCompetitorPO;
import de.hatoka.cashgame.internal.persistence.CashGameDao;
import de.hatoka.cashgame.internal.persistence.CashGamePO;
import de.hatoka.common.capi.math.Money;
import de.hatoka.common.capi.persistence.MoneyPOConverter;
import de.hatoka.player.capi.business.HistoryBORepository;
import de.hatoka.player.capi.business.HistoryEntryBO;
import de.hatoka.player.capi.business.PlayerBO;
import de.hatoka.user.capi.business.UserRef;

@Component
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class CashGameBOImpl implements CashGameBO
{
    @Autowired
    private CashGameDao cashGameDao;
    @Autowired
    private CashGameCompetitorDao competitorDao;
    @Autowired
    private CompetitorBOFactory competitorFactory;
    @Autowired
    private HistoryBORepository historyRepository;

    private CashGamePO cashGamePO;
    private Long cashGame_id;

    public CashGameBOImpl(CashGamePO cashGamePO)
    {
        this.cashGamePO = cashGamePO;
        this.cashGame_id = cashGamePO.getInternalID();
    }

    @Override
    public CompetitorBO sitDown(PlayerBO playerBO, Money buyIn)
    {
        CashGameCompetitorPO po = new CashGameCompetitorPO();
        po.setPlayerRef(playerBO.getRef().getGlobalRef());
        po.setCashGameID(cashGame_id);
        CompetitorBO bo = competitorFactory.get(competitorDao.save(po), this);
        bo.buyin(buyIn == null ? getBuyIn() : buyIn);
        return bo;
    }

    @Override
    public Collection<CompetitorBO> getActiveCompetitors()
    {
        return getActiveCompetitorBOStream().collect(Collectors.toList());
    }

    private Stream<ICompetitorBO> getActiveCompetitorBOStream()
    {
        return getCompetitorBOStream().filter(competitor -> !CompetitorState.OUT.equals(competitor.getState()));
    }

    @Override
    public Money getAverageInplay()
    {
        Money sum = getSumInplay();
        if (Money.NOTHING.equals(sum))
        {
            return sum;
        }
        long activeCompetitors = getActiveCompetitorBOStream().count();
        return sum.divide(activeCompetitors);
    }

    @Override
    public Money getBuyIn()
    {
        return MoneyPOConverter.valueOf(cashGamePO.getBuyIn());
    }

    @Override
    public List<CompetitorBO> getCompetitors()
    {
        return getCompetitorBOStream().sorted(CashGameComparators.NAME).collect(Collectors.toList());
    }

    private Stream<ICompetitorBO> getCompetitorBOStream()
    {
        return getCompetitorPOs().stream().map(c -> competitorFactory.get(c, this));
    }

    private List<CashGameCompetitorPO> getCompetitorPOs()
    {
        return competitorDao.getByCashGameID(cashGame_id);
    }

    @Override
    public Date getDate()
    {
        return cashGamePO.getStartDate();
    }

    @Override
    public Money getSumInplay()
    {
        Money sum = Money.NOTHING;
        for (CompetitorBO competitor : getCompetitors())
        {
            sum = sum.add(competitor.getInPlay());
        }
        return sum;
    }

    @Override
    public boolean isCompetitor(PlayerBO player)
    {
        String playerRef = player.getRef().getGlobalRef();
        return getCompetitorPOs().stream().anyMatch(competitorPO -> playerRef.equals(competitorPO.getPlayerRef()));
    }

    @Override
    public void remove()
    {
        historyRepository.deleteEntries(getRef().getGlobalRef());
        competitorDao.deleteAllInBatch(competitorDao.getByCashGameID(cashGame_id));
        cashGameDao.deleteById(cashGame_id);
        cashGame_id = null;
        cashGamePO = null;
    }

    @Override
    public void setBuyIn(Money buyIn)
    {
        cashGamePO.setBuyIn(MoneyPOConverter.persistence(buyIn));
        savePO();
    }

    @Override
    public void unassign(CompetitorBO competitorBO)
    {
        if (competitorBO.isActive() || !competitorBO.getResult().isZero())
        {
            throw new IllegalStateException("Can't remove competitor, is/was in play with result.");
        }
        competitorBO.remove();
    }

    @Override
    public int hashCode()
    {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((cashGamePO == null) ? 0 : cashGamePO.hashCode());
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
        CashGameBOImpl other = (CashGameBOImpl)obj;
        if (cashGamePO == null)
        {
            if (other.cashGamePO != null)
                return false;
        }
        else if (!cashGamePO.equals(other.cashGamePO))
            return false;
        return true;
    }

    @Override
    public void sortCompetitors()
    {
        int position = 1;
        for (ICompetitorBO competitorBO : getCompetitorBOStream().sorted(CashGameComparators.RESULT).collect(
                        Collectors.toList()))
        {
            competitorBO.setPosition(position++);
        }
    }

    @Override
    public List<HistoryEntryBO> getHistoryEntries()
    {
        return historyRepository.getEntries(getRef().getGlobalRef());
    }

    @Override
    public Collection<CompetitorBO> getCashGameCompetitors()
    {
        return getCompetitorBOStream().collect(Collectors.toList());
    }

    private void savePO()
    {
        cashGamePO = cashGameDao.save(cashGamePO);
    }

    @Override
    public CashGameRef getRef()
    {
        return CashGameRef.valueOf(UserRef.valueOfGlobal(cashGamePO.getOwnerRef()), cashGamePO.getLocalRef());
    }
}
