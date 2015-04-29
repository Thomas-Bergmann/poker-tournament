package de.hatoka.tournament.capi.business;

import java.util.Date;
import java.util.List;

public interface TournamentBO extends GameBO
{
    /**
     * @return the identifier (artificial key)
     */
    String getID();

    /**
     * Removes that object
     */
    void remove();

    /**
     * @return the start date and time of the tournament
     */
    Date getStartTime();

    /**
     * set start date and time of the tournament
     */
    void setStartTime(Date date);

    /**
     * @return name of tournament
     */
    String getName();

    /**
     * Set name of tournament
     */
    void setName(String name);

    /**
     * Creates a new blind level for the tournament
     *
     * @param duration
     * @param smallBlind
     * @param bigBlind
     * @param ante
     * @return
     */
    BlindLevelBO createBlindLevel(int duration, int smallBlind, int bigBlind, int ante);

    /**
     * Creates a new blind level for the tournament
     *
     * @param duration
     * @return
     */
    TournamentRoundBO createPause(int duration);

    /**
     * @return rounds of tournament
     */
    List<TournamentRoundBO> getTournamentRoundBOs();

    /**
     * Removes a previously created pause or blind level
     * @param round
     */
    void remove(TournamentRoundBO round);
}
