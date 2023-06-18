package de.hatoka.player.capi.business;

import java.util.Date;
import java.util.List;

import de.hatoka.common.capi.math.Money;
import de.hatoka.player.capi.types.HistoryEntryType;

public interface HistoryBORepository
{
    /**
     * Creates new entry for player
     */
    HistoryEntryBO createEntry(Date date, PlayerRef playerRef, String gameRef, HistoryEntryType type, Money amount);

    /**
     * @param playerRef player reference
     * @return list of entries for known player by user
     */
    List<HistoryEntryBO> getEntries(PlayerRef playerRef);

    /**
     * @param gameRef global game reference
     * @return list of entries by user
     */
    List<HistoryEntryBO> getEntries(String gameRef);

    void deleteEntries(String gameRef);
}
