package de.hatoka.tournament.capi.business;

import java.util.Date;
import java.util.List;

import de.hatoka.user.capi.business.UserRef;

/**
 * Access to tournaments of an user
 */
public interface TournamentBORepository
{
    TournamentBO createTournament(UserRef userRef, String localTournamentRef, Date startDate, String name);
    List<TournamentBO> getTournaments(UserRef userRef);
}
