package de.hatoka.player.capi.business;

import java.util.Date;

/**
 * A player can attend at multiple tournaments as competitor. A player is a real
 * person and can be an user, but doesn't need to be. Players can be registered in context of
 * an user (known player), a tournament (competitor) or cash game (visitor)
 */
public interface PlayerBO
{
    /**
     * @return unique identifier of player in specific context.
     */
	PlayerRef getRef();

	/**
     * @return name of player (display)
     */
    String getName();

    /**
     * @param name of player (display)
     */
    void setName(String name);

    /**
     * @return email address used to find players of an user.
     */
    String getEMail();

    /**
     * Sets email address of a player
     */
    void setEMail(String email);

    /**
     * Removes player from user/account
     */
    void remove();

    Date getFirstDate();
}
