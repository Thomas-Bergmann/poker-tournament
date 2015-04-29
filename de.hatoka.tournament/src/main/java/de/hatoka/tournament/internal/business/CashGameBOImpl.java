package de.hatoka.tournament.internal.business;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import de.hatoka.common.capi.business.Money;
import de.hatoka.tournament.capi.business.CompetitorBO;
import de.hatoka.tournament.capi.business.HistoryEntryBO;
import de.hatoka.tournament.capi.business.PlayerBO;
import de.hatoka.tournament.capi.business.CashGameBO;
import de.hatoka.tournament.capi.business.TournamentBusinessFactory;
import de.hatoka.tournament.capi.dao.CompetitorDao;
import de.hatoka.tournament.capi.dao.PlayerDao;
import de.hatoka.tournament.capi.dao.TournamentDao;
import de.hatoka.tournament.capi.entities.CompetitorPO;
import de.hatoka.tournament.capi.entities.HistoryPO;
import de.hatoka.tournament.capi.entities.PlayerPO;
import de.hatoka.tournament.capi.entities.TournamentPO;

public class CashGameBOImpl implements CashGameBO
{
    private TournamentPO tournamentPO;
    private final TournamentDao tournamentDao;
    private final CompetitorDao competitorDao;
    private final PlayerDao playerDao;
    private final TournamentBusinessFactory factory;

    public CashGameBOImpl(TournamentPO tournamentPO, TournamentDao tournamentDao, CompetitorDao competitorDao,
                    PlayerDao playerDao, TournamentBusinessFactory factory)
    {
        this.tournamentPO = tournamentPO;
        this.tournamentDao = tournamentDao;
        this.competitorDao = competitorDao;
        this.playerDao = playerDao;
        this.factory = factory;
    }

    @Override
    public CompetitorBO assign(PlayerBO playerBO)
    {

        PlayerPO playerPO = playerDao.getById(playerBO.getID());
        if (playerPO == null)
        {
            throw new IllegalArgumentException("Can't resolve persistent object for playerBO:" + playerBO.getID());
        }
        return getBO(competitorDao.createAndInsert(tournamentPO, playerPO));
    }

    @Override
    public Collection<CompetitorBO> getActiveCompetitors()
    {
        return getActiveCompetitorBOStream().collect(Collectors.toList());
    }

    private Stream<CompetitorBO> getActiveCompetitorBOStream()
    {
        return getCompetitorBOStream().filter(competitor -> competitor.isActive());
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

    private CompetitorBO getBO(CompetitorPO competitorPO)
    {
        return factory.getCompetitorBO(competitorPO, this);
    }

    @Override
    public Money getBuyIn()
    {
        return Money.getInstance(tournamentPO.getBuyIn());
    }

    @Override
    public List<CompetitorBO> getCompetitors()
    {
        return getCompetitorBOStream().sorted(CompetitorBOComparators.POSITION).collect(Collectors.toList());
    }

    private Stream<CompetitorBO> getCompetitorBOStream()
    {
        return tournamentPO.getCompetitors().stream().map(competitorPO -> getBO(competitorPO));
    }

    @Override
    public Date getDate()
    {
        return tournamentPO.getDate();
    }

    @Override
    public String getID()
    {
        return tournamentPO.getId();
    }

    @Override
    public String getName()
    {
        return tournamentPO.getName();
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
        String playerID = player.getID();
        return tournamentPO.getCompetitors().stream()
                        .anyMatch(competitorPO -> competitorPO.getPlayerPO().getId().equals(playerID));
    }

    @Override
    public void remove()
    {
        getCompetitors().stream().forEach(competitor -> remove(competitor));
        tournamentDao.remove(tournamentPO);
        tournamentPO = null;
    }

    @Override
    public void setBuyIn(Money buyIn)
    {
        tournamentPO.setBuyIn(buyIn.toMoneyPO());
    }

    @Override
    public void unassign(CompetitorBO competitorBO)
    {
        if (competitorBO.isActive() || !competitorBO.getResult().isZero())
        {
            throw new IllegalStateException("Can't remove competitor, is/was in play with result.");
        }
        remove(competitorBO);
    }

    private void remove(CompetitorBO competitorBO)
    {
        competitorDao.remove(competitorBO.getID());
    }

    @Override
    public int hashCode()
    {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((tournamentPO == null) ? 0 : tournamentPO.hashCode());
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
        if (tournamentPO == null)
        {
            if (other.tournamentPO != null)
                return false;
        }
        else if (!tournamentPO.equals(other.tournamentPO))
            return false;
        return true;
    }

    @Override
    public void sortCompetitors()
    {
        int position = 1;
        for (CompetitorBO competitorBO : getCompetitorBOStream().sorted(CompetitorBOComparators.DEFAULT).collect(
                        Collectors.toList()))
        {
            competitorBO.setPosition(position++);
        }
    }

    @Override
    public List<HistoryEntryBO> getHistoryEntries()
    {
        List<HistoryEntryBO> result = new ArrayList<>();
        for(HistoryPO historyPO : tournamentPO.getHistoryEntries())
        {
            result.add(factory.getHistoryBO(historyPO));
        }
        return result;
    }
}
