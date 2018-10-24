package de.hatoka.tournament.internal.business;

import de.hatoka.tournament.internal.persistence.RankPO;

public interface RankBOFactory
{
    IRankBO get(RankPO po, ITournamentBO tournamentBO);
}
