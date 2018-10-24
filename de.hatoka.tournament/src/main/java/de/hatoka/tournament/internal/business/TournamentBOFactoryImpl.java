package de.hatoka.tournament.internal.business;

import org.springframework.beans.factory.annotation.Lookup;
import org.springframework.stereotype.Component;

import de.hatoka.tournament.capi.business.TournamentBO;
import de.hatoka.tournament.internal.persistence.TournamentPO;

@Component
public class TournamentBOFactoryImpl implements TournamentBOFactory
{
    @Lookup
    @Override
    public TournamentBO get(TournamentPO po)
    {
        // done by @Lookup
        return null;
    }
}
