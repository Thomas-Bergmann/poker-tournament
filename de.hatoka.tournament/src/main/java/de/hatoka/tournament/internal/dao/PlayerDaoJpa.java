package de.hatoka.tournament.internal.dao;

import java.util.List;

import javax.inject.Inject;

import de.hatoka.common.capi.dao.GenericJPADao;
import de.hatoka.common.capi.dao.UUIDGenerator;
import de.hatoka.tournament.capi.dao.CompetitorDao;
import de.hatoka.tournament.capi.dao.PlayerDao;
import de.hatoka.tournament.capi.entities.PlayerPO;

public class PlayerDaoJpa extends GenericJPADao<PlayerPO> implements PlayerDao
{
    @Inject
    private UUIDGenerator uuidGenerator;

    @Inject
    private CompetitorDao competitorDao;

    public PlayerDaoJpa()
    {
        super(PlayerPO.class);
    }

    @Override
    public PlayerPO createAndInsert(String accountRef, String name)
    {
        PlayerPO result = create();
        result.setId(uuidGenerator.generate());
        result.setAccountRef(accountRef);
        result.setName(name);
        insert(result);
        return result;
    }

    @Override
    public PlayerPO findByName(String name)
    {
        return createNamedQuery("PlayerPO.findByName").setParameter("name", name).getSingleResult();
    }

    @Override
    public List<PlayerPO> getByAccountRef(String accountRef)
    {
        return createNamedQuery("PlayerPO.findByAccountRef").setParameter("accountRef", accountRef).getResultList();
    }

    @Override
    public void remove(PlayerPO playerPO)
    {
        playerPO.getCompetitors().forEach(competitorPO -> competitorDao.remove(competitorPO));
        super.remove(playerPO);
    }
}
