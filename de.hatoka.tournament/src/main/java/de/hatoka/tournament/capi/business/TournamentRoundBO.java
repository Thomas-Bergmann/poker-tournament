package de.hatoka.tournament.capi.business;

public interface TournamentRoundBO
{
   /**
    *
    * @return the identifier (signature) of the business object
    */
   String getID();

   /**
    * @return duration of round
    */
   Integer getDuration();

   boolean isRebuyAllowed();

   void allowRebuy(boolean allow);

   /**
    * @return BlindLevel in case it's not a pause
    */
   BlindLevelBO getBlindLevel();
}
