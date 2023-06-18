package de.hatoka.tournament.capi.business;

import java.util.Date;

public interface TournamentRoundBO
{
    /**
     * @return position of round (blind level or pause)
     */
    Integer getPosition();

    /**
    * @return duration of round
    */
   Integer getDuration();

   boolean isRebuyAllowed();

   void allowRebuy(boolean allow);

   /**
    * @return BlindLevel in case it's a blind level and not a pause
    */
   BlindLevelBO getBlindLevel();

   /**
    * starts the round, deactivates the current round of the tournament and set the start date of round to current date
    */
   void start();

   /**
    * @return the start date (time) of the round
    */
   Date getStartTime();

   /**
    * @return the start date (time) of the round
    */
   Date getEndTime();

    /**
    * @return true in case this round is active
    */
    boolean isActive();
}
