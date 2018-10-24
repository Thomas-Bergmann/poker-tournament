package de.hatoka.player.capi.business;

import java.util.Date;

import de.hatoka.common.capi.math.Money;
import de.hatoka.player.capi.types.HistoryEntryType;

/**
 * A player can attend at multiple tournaments as competitor
 */
public interface HistoryEntryBO
{
    /**
     *
     * @return date of history entry
     */
    Date getDate();

    /**
     * @return type of entry
     */
    HistoryEntryType getType();

    /**
     * @return player who does the action
     */
    PlayerBO getPlayer();

    /**
     * @return reference to the game
     */
    String getGameRef();
    /**
     * @return amount
     */
    Money getAmount();
}
