package de.hatoka.tournament.capi.business;

/**
 * A player can attend at multiple tournaments as competitor
 */
public interface PlayerBO
{
    /**
    *
    * @return the identifier (artificial key)
    */
   String getID();

   /**
    * Removes that object
    */
   void remove();

   /**
    * @return name of player (semantic key)
    */
   String getName();

   /**
    * @return email address used to find players of an user.
    */
   String getEMail();

   /**
    * Sets email address of a player
    */
   void seetEMail(String email);
}
