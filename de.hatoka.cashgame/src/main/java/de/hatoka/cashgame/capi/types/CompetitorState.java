package de.hatoka.cashgame.capi.types;

public enum CompetitorState
{
    /**
     * player is playing
     */
    ACTIVE,

    /**
     * player is not playing, but has not payed
     */
    INACTIVE,

    /**
     * player is out of tournament (has played)
     */
    OUT;
}
