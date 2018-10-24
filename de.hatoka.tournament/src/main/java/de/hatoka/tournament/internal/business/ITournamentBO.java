package de.hatoka.tournament.internal.business;

import de.hatoka.tournament.capi.business.TournamentBO;

public interface ITournamentBO extends TournamentBO
{
    void defineBlindLevelStartTimes();

    /**
     * starts the pause
     */
    void start(IPauseBO pause);

    Integer getCurrentRound();

    /**
     * starts the blind level
     */
    void start(IBlindLevelBO blindLevel);

    /**
     * inform tournament about deletion of rank
     */
    void removeRank(IRankBO rank);
}
