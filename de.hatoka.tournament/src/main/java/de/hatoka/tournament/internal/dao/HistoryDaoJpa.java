package de.hatoka.tournament.internal.dao;

import java.util.Date;

import javax.inject.Inject;

import de.hatoka.common.capi.dao.GenericJPADao;
import de.hatoka.common.capi.dao.UUIDGenerator;
import de.hatoka.tournament.capi.dao.HistoryDao;
import de.hatoka.tournament.capi.entities.HistoryPO;
import de.hatoka.tournament.capi.entities.TournamentPO;

public class HistoryDaoJpa extends GenericJPADao<HistoryPO> implements HistoryDao
{
    @Inject
    private UUIDGenerator uuidGenerator;

    public HistoryDaoJpa()
    {
        super(HistoryPO.class);
    }

    @Override
    public HistoryPO createAndInsert(TournamentPO tournamentPO, String player, Date date)
    {
        HistoryPO result = create();
        result.setId(uuidGenerator.generate());
        result.setAccountRef(tournamentPO.getAccountRef());
        result.setDate(date);
        result.setPlayer(player);
        // set relations
        result.setTournamentPO(tournamentPO);
        tournamentPO.getHistoryEntries().add(result);
        insert(result);
        return result;
    }


    @Override
    public void remove(HistoryPO historyPO)
    {
        // remove relations
        TournamentPO tournament = historyPO.getTournamentPO();
        if (tournament != null)
        {
            tournament.getHistoryEntries().remove(historyPO);
            historyPO.setTournamentPO(null);
        }
        super.remove(historyPO);
    }
}
