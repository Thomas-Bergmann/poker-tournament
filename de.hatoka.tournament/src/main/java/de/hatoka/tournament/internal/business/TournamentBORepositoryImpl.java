package de.hatoka.tournament.internal.business;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import de.hatoka.tournament.capi.business.TournamentBO;
import de.hatoka.tournament.capi.business.TournamentBORepository;
import de.hatoka.tournament.capi.business.TournamentBusinessFactory;
import de.hatoka.tournament.capi.dao.TournamentDao;
import de.hatoka.tournament.capi.entities.TournamentPO;

public class TournamentBORepositoryImpl implements TournamentBORepository
{
    private final String accountRef;

    private final TournamentDao tournamentDao;

    private final TournamentBusinessFactory factory;

    public TournamentBORepositoryImpl(String accountRef, TournamentDao tournamentDao, TournamentBusinessFactory factory)
    {
        this.accountRef = accountRef;
        this.tournamentDao = tournamentDao;
        this.factory = factory;
    }

    @Override
    public TournamentBO create(String name, Date date)
    {
        TournamentPO tournamentPO = tournamentDao.createAndInsert(accountRef, name, date);
        return getBO(tournamentPO);
    }

    private TournamentBO getBO(TournamentPO tournamentPO)
    {
        return factory.getTournamentBO(tournamentPO);
    }

    @Override
    public TournamentBO getByID(String id)
    {
        TournamentPO tournamentPO = tournamentDao.getById(id);
        if (!accountRef.equals(tournamentPO.getAccountRef()))
        {
            throw new IllegalArgumentException("tournament not assigned to account");
        }
        return getBO(tournamentPO);
    }

    @Override
    public List<TournamentBO> getTournamenBOs()
    {
        return tournamentDao.getByAccountRef(accountRef).stream().map(tournamentPO -> getBO(tournamentPO)).collect(Collectors.toList());
    }

}
