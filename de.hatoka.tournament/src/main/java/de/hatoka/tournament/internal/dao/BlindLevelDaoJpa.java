package de.hatoka.tournament.internal.dao;

import javax.inject.Inject;

import de.hatoka.common.capi.dao.GenericJPADao;
import de.hatoka.common.capi.dao.UUIDGenerator;
import de.hatoka.tournament.capi.dao.BlindLevelDao;
import de.hatoka.tournament.capi.entities.BlindLevelPO;
import de.hatoka.tournament.capi.entities.TournamentPO;

public class BlindLevelDaoJpa extends GenericJPADao<BlindLevelPO> implements BlindLevelDao
{
    @Inject
    private UUIDGenerator uuidGenerator;

    public BlindLevelDaoJpa()
    {
        super(BlindLevelPO.class);
    }

    @Override
    public BlindLevelPO createAndInsert(TournamentPO tournamentPO, Integer duration)
    {
        BlindLevelPO result = create();
        result.setId(uuidGenerator.generate());
        result.setTournamentPO(tournamentPO);
        result.setDuration(duration);
        insert(result);
        tournamentPO.getBlindLevels().add(result);
        return result;
    }


    @Override
    public void remove(BlindLevelPO blindLevelPO)
    {
        blindLevelPO.getTournamentPO().getBlindLevels().remove(blindLevelPO);
        super.remove(blindLevelPO);
    }
}
