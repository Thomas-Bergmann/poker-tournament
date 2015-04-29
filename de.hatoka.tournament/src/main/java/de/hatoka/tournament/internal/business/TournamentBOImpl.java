package de.hatoka.tournament.internal.business;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import de.hatoka.common.capi.business.Money;
import de.hatoka.tournament.capi.business.BlindLevelBO;
import de.hatoka.tournament.capi.business.CompetitorBO;
import de.hatoka.tournament.capi.business.HistoryEntryBO;
import de.hatoka.tournament.capi.business.PlayerBO;
import de.hatoka.tournament.capi.business.TournamentBO;
import de.hatoka.tournament.capi.business.TournamentBusinessFactory;
import de.hatoka.tournament.capi.business.TournamentRoundBO;
import de.hatoka.tournament.capi.dao.BlindLevelDao;
import de.hatoka.tournament.capi.dao.CompetitorDao;
import de.hatoka.tournament.capi.dao.PlayerDao;
import de.hatoka.tournament.capi.dao.TournamentDao;
import de.hatoka.tournament.capi.entities.BlindLevelPO;
import de.hatoka.tournament.capi.entities.CompetitorPO;
import de.hatoka.tournament.capi.entities.HistoryEntryType;
import de.hatoka.tournament.capi.entities.HistoryPO;
import de.hatoka.tournament.capi.entities.PlayerPO;
import de.hatoka.tournament.capi.entities.TournamentPO;

public class TournamentBOImpl implements TournamentBO
{
    private TournamentPO tournamentPO;
    private final TournamentDao tournamentDao;
    private final CompetitorDao competitorDao;
    private final PlayerDao playerDao;
    private final BlindLevelDao blindLevelDao;
    private final TournamentBusinessFactory factory;

    public TournamentBOImpl(TournamentPO tournamentPO, TournamentDao tournamentDao, CompetitorDao competitorDao,
                    PlayerDao playerDao, BlindLevelDao blindLevelDao, TournamentBusinessFactory factory)
    {
        this.tournamentPO = tournamentPO;
        this.tournamentDao = tournamentDao;
        this.competitorDao = competitorDao;
        this.playerDao = playerDao;
        this.blindLevelDao = blindLevelDao;
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
    public Date getStartTime()
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
        TournamentBOImpl other = (TournamentBOImpl)obj;
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

    @Override
    public void setStartTime(Date date)
    {
        tournamentPO.setDate(date);
    }

    @Override
    public BlindLevelBO createBlindLevel(int duration, int smallBlind, int bigBlind, int ante)
    {
        BlindLevelPO blindLevelPO = blindLevelDao.createAndInsert(tournamentPO, duration);
        blindLevelPO.setPause(false);
        blindLevelPO.setSmallBlind(smallBlind);
        blindLevelPO.setBigBlind(bigBlind);
        blindLevelPO.setAnte(ante);
        return factory.getBlindLevelBO(blindLevelPO);
    }

    @Override
    public TournamentRoundBO createPause(int duration)
    {
        BlindLevelPO blindLevelPO = blindLevelDao.createAndInsert(tournamentPO, duration);
        blindLevelPO.setPause(true);
        return factory.getPauseBO(blindLevelPO);
    }

    @Override
    public void setName(String name)
    {
        tournamentPO.setName(name);
    }

    @Override
    public List<TournamentRoundBO> getTournamentRoundBOs()
    {
        List<BlindLevelPO> blindLevels = tournamentPO.getBlindLevels();
        List<TournamentRoundBO> result = new ArrayList<TournamentRoundBO>(blindLevels.size());
        for(BlindLevelPO blindLevelPO : blindLevels)
        {
            if (blindLevelPO.isPause())
            {
                result.add(factory.getPauseBO(blindLevelPO));
            }
            else
            {
                result.add(factory.getBlindLevelBO(blindLevelPO));
            }
        }
        return result;
    }

    @Override
    public void remove(TournamentRoundBO round)
    {
        Iterator<BlindLevelPO> blindLevels = tournamentPO.getBlindLevels().iterator();
        while(blindLevels.hasNext())
        {
            BlindLevelPO blindLevelPO = blindLevels.next();
            if (blindLevelPO.getId().equals(round.getID()))
            {
                blindLevelDao.remove(blindLevelPO);
                break;
            }
        }
    }

    @Override
    public Money getReBuy()
    {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void seatOpen(CompetitorBO competitorBO)
    {
        if (!competitorBO.isActive() || !(competitorBO instanceof TournamentCompetitor))
        {
            throw new IllegalStateException("seatOpen not allowed at inactive competitors");
        }
        Money moneyResult = getResultForNextLooser();
        TournamentCompetitor tournamentCompetitor = (TournamentCompetitor) competitorBO;
        tournamentCompetitor.setActive(false);
        tournamentCompetitor.setResult(moneyResult);
        sortCompetitors();
        tournamentCompetitor.createEntry(HistoryEntryType.CashOut, moneyResult);
    }

    private Money getResultForNextLooser()
    {
        // TODO Auto-generated method stub
        return null;
    }
}
