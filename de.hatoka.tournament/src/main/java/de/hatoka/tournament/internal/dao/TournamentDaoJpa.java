package de.hatoka.tournament.internal.dao;

import java.util.Date;
import java.util.List;

import javax.inject.Inject;

import de.hatoka.common.capi.dao.GenericJPADao;
import de.hatoka.common.capi.dao.UUIDGenerator;
import de.hatoka.tournament.capi.dao.CompetitorDao;
import de.hatoka.tournament.capi.dao.TournamentDao;
import de.hatoka.tournament.capi.entities.TournamentPO;

public class TournamentDaoJpa extends GenericJPADao<TournamentPO> implements TournamentDao
{
    @Inject
    private UUIDGenerator uuidGenerator;

    @Inject
    private CompetitorDao competitorDao;

    public TournamentDaoJpa()
    {
        super(TournamentPO.class);
    }

    @Override
    public TournamentPO createAndInsert(String accountRef, String name, Date date, boolean isCashGame)
    {
        TournamentPO result = create();
        result.setId(uuidGenerator.generate());
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
    public void remove(TournamentPO tournamentPO)
    {
        tournamentPO.getCompetitors().forEach(competitorPO -> competitorDao.remove(competitorPO));
        super.remove(tournamentPO);
    }
}
