package de.hatoka.tournament.internal.dao;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.inject.Inject;

import de.hatoka.common.capi.dao.GenericJPADao;
import de.hatoka.common.capi.dao.UUIDGenerator;
import de.hatoka.tournament.capi.dao.BlindLevelDao;
import de.hatoka.tournament.capi.dao.CompetitorDao;
import de.hatoka.tournament.capi.dao.HistoryDao;
import de.hatoka.tournament.capi.dao.TournamentDao;
import de.hatoka.tournament.capi.entities.TournamentPO;

public class TournamentDaoJpa extends GenericJPADao<TournamentPO> implements TournamentDao
{
    @Inject
    private UUIDGenerator uuidGenerator;
    @Inject
    private CompetitorDao competitorDao;
    @Inject
    private BlindLevelDao blindLevelDao;
    @Inject
    private HistoryDao historyDao;

    public TournamentDaoJpa()
    {
        super(TournamentPO.class);
    }

    @Override
    public TournamentPO createAndInsert(String accountRef, String externalRef, String name, Date date, boolean isCashGame)
    {
        TournamentPO result = create();
        result.setId(uuidGenerator.generate());
        result.setExternalRef(externalRef);
        result.setAccountRef(accountRef);
        result.setName(name);
        result.setDate(date);
        result.setCashGame(isCashGame);
        insert(result);
        return result;
    }

    @Override
    public List<TournamentPO> getByAccountRef(String accountRef)
    {
        return createNamedQuery("TournamentPO.findByAccountRef").setParameter("accountRef", accountRef).getResultList();
    }

    @Override
    public TournamentPO findByExternalRef(String accountRef, String externalRef)
    {
        return getOptionalResult(createNamedQuery("TournamentPO.findByExternalRef").setParameter("accountRef", accountRef).setParameter("externalRef", externalRef));
    }


    @Override
    public void remove(TournamentPO tournamentPO)
    {
        new ArrayList<>(tournamentPO.getCompetitors()).forEach(element -> competitorDao.remove(element));
        new ArrayList<>(tournamentPO.getBlindLevels()).forEach(element -> blindLevelDao.remove(element));
        new ArrayList<>(tournamentPO.getHistoryEntries()).forEach(element -> historyDao.remove(element));
        super.remove(tournamentPO);
    }

}
