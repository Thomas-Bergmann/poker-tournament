package de.hatoka.tournament.internal.business;

import org.springframework.beans.factory.annotation.Lookup;
import org.springframework.stereotype.Component;

import de.hatoka.tournament.capi.business.BlindLevelBO;
import de.hatoka.tournament.internal.persistence.BlindLevelPO;

@Component
public class BlindLevelBOFactoryImpl implements BlindLevelBOFactory
{
    @Lookup
    @Override
    public BlindLevelBO get(BlindLevelPO po, ITournamentBO tournament)
    {
        // done by @Lookup
        return null;
    }
}
