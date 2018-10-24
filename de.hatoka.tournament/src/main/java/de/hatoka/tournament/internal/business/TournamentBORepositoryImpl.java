package de.hatoka.tournament.internal.business;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import de.hatoka.tournament.capi.business.TournamentBO;
import de.hatoka.tournament.capi.business.TournamentBORepository;
import de.hatoka.tournament.internal.persistence.TournamentDao;
import de.hatoka.tournament.internal.persistence.TournamentPO;
import de.hatoka.user.capi.business.UserRef;

@Component
public class TournamentBORepositoryImpl implements TournamentBORepository
{
    @Autowired
    private TournamentBOFactory tournamentFactory;
    @Autowired
    private TournamentDao tournamentDao;

    @Override
    public TournamentBO createTournament(UserRef userRef, String localTournamentRef, Date startDate, String name)
    {
        return tournamentFactory.get(createTournamentPO(userRef, localTournamentRef, name, startDate));
    }

    private TournamentPO createTournamentPO(UserRef userRef, String localTournamentRef, String name, Date startDate)
    {
        TournamentPO tournamentPO = new TournamentPO();
        tournamentPO.setOwnerRef(userRef.getGlobalRef());
        tournamentPO.setLocalRef(localTournamentRef);
        tournamentPO.setName(name);
        tournamentPO.setStartDate(startDate);
        return tournamentDao.save(tournamentPO);
    }

    @Override
    public List<TournamentBO> getTournaments(UserRef userRef)
    {
        return tournamentDao.getByOwnerRef(userRef.getGlobalRef()).stream()
                        .map(tournamentFactory::get)
                        .collect(Collectors.toList());
    }
}
