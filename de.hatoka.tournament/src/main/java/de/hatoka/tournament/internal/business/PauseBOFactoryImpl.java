package de.hatoka.tournament.internal.business;

import org.springframework.beans.factory.annotation.Lookup;
import org.springframework.stereotype.Component;

import de.hatoka.tournament.capi.business.PauseBO;
import de.hatoka.tournament.internal.persistence.BlindLevelPO;

@Component
public class PauseBOFactoryImpl implements PauseBOFactory
{
    @Lookup
    @Override
    public PauseBO get(BlindLevelPO po, ITournamentBO tournament)
    {
        // done by @Lookup
        return null;
    }
}
