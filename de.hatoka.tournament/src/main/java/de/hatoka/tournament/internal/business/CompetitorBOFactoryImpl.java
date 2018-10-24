package de.hatoka.tournament.internal.business;

import org.springframework.beans.factory.annotation.Lookup;
import org.springframework.stereotype.Component;

import de.hatoka.tournament.capi.business.TournamentBO;
import de.hatoka.tournament.internal.persistence.CompetitorPO;

@Component("TournamentCompetitorBOFactory")
public class CompetitorBOFactoryImpl implements CompetitorBOFactory
{
    @Override
    @Lookup
    public ICompetitorBO get(CompetitorPO po, TournamentBO tournament)
    {
        // done by @Lookup
        return null;
    }
}
