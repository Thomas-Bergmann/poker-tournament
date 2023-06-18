package de.hatoka.tournament.internal.business;

import de.hatoka.tournament.capi.business.BlindLevelBO;
import de.hatoka.tournament.internal.persistence.BlindLevelPO;

public interface BlindLevelBOFactory
{
    BlindLevelBO get(BlindLevelPO po, ITournamentBO tournament);
}
