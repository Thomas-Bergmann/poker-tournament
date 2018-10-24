package de.hatoka.tournament.internal.business;

import org.springframework.beans.factory.annotation.Lookup;
import org.springframework.stereotype.Component;

import de.hatoka.tournament.internal.persistence.RankPO;

@Component
public class RankBOFactoryImpl implements RankBOFactory
{
    @Lookup
    @Override
    public IRankBO get(RankPO po, ITournamentBO tournamentBO)
    {
        // done by @Lookup
        return null;
    }
}
