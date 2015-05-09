package de.hatoka.tournament.capi.business;

import java.util.Date;

import de.hatoka.common.capi.business.Money;
import de.hatoka.tournament.capi.types.HistoryEntryType;

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
     * @return amount
     */
    Money getAmount();

    /**
     * @return player who does the action
     */
    PlayerBO getPlayer();
}
