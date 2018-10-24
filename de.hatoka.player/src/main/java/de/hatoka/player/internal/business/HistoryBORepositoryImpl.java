package de.hatoka.player.internal.business;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import de.hatoka.common.capi.math.Money;
import de.hatoka.common.capi.persistence.MoneyPOConverter;
import de.hatoka.player.capi.business.HistoryBORepository;
import de.hatoka.player.capi.business.HistoryEntryBO;
import de.hatoka.player.capi.business.PlayerRef;
import de.hatoka.player.capi.types.HistoryEntryType;
import de.hatoka.player.internal.persistence.HistoryDao;
import de.hatoka.player.internal.persistence.HistoryPO;

@Component
public class HistoryBORepositoryImpl implements HistoryBORepository
{
    @Autowired
    private HistoryDao historyDao;
    @Autowired
    private HistoryBOFactory factory;

    @Override
    public HistoryEntryBO createEntry(Date date, PlayerRef playerRef, String gameRef, HistoryEntryType type,
                    Money amount)
    {
        HistoryPO entry = new HistoryPO();
        entry.setGameRef(gameRef);
        entry.setAmount(MoneyPOConverter.persistence(amount));
        entry.setDate(date);
        entry.setType(type);
        entry.setPlayerRef(playerRef.getGlobalRef());
        return factory.get(historyDao.save(entry));
    }

    @Override
    public List<HistoryEntryBO> getEntries(PlayerRef playerRef)
    {
        return historyDao.getByPlayerRef(playerRef.getGlobalRef()).stream().map(factory::get).collect(Collectors.toList());
    }

    @Override
    public List<HistoryEntryBO> getEntries(String gameRef)
    {
        return historyDao.getByGameRef(gameRef).stream().map(factory::get).collect(Collectors.toList());
    }

    @Override
    public void deleteEntries(String gameRef)
    {
        historyDao.deleteAllInBatch(historyDao.getByGameRef(gameRef));
    }
}
