package de.hatoka.tournament.capi.types;

public enum CompetitorState
{
    /**
     * player is registered
     */
    REGISTERED,

    /**
     * player is activly playing
     */
    ACTIVE,

    /**
     * player is out of tournament (has played)
     */
    OUT;
}
