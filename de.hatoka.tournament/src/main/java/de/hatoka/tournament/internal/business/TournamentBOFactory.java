package de.hatoka.tournament.internal.business;

import de.hatoka.tournament.capi.business.TournamentBO;
import de.hatoka.tournament.internal.persistence.TournamentPO;

public interface TournamentBOFactory
{
    TournamentBO get(TournamentPO tournamentPO);
}
