package de.hatoka.tournament.internal.business;

import de.hatoka.tournament.capi.business.TournamentBO;
import de.hatoka.tournament.internal.persistence.CompetitorPO;

public interface CompetitorBOFactory
{
    ICompetitorBO get(CompetitorPO po, TournamentBO tournament);
}
