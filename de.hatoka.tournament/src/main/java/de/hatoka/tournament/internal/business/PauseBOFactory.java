package de.hatoka.tournament.internal.business;

import de.hatoka.tournament.capi.business.PauseBO;
import de.hatoka.tournament.internal.persistence.BlindLevelPO;

public interface PauseBOFactory
{
    PauseBO get(BlindLevelPO po, ITournamentBO tournament);
}
