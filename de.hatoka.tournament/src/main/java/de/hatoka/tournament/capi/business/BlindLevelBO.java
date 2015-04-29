package de.hatoka.tournament.capi.business;


public interface BlindLevelBO extends TournamentRoundBO
{
    Integer getSmallBlind();

    Integer getBigBlind();

    Integer getAnte();
}
