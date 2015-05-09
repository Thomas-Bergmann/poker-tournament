package de.hatoka.tournament.capi.business;

import de.hatoka.common.capi.business.Money;

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

   /**
    * @return BlindLevel in case it's not a pause
    */
   BlindLevelBO getBlindLevel();

   /**
    * @return the re-buy amount (null if no re-buy allowed)
    */
   Money getReBuy();
}
