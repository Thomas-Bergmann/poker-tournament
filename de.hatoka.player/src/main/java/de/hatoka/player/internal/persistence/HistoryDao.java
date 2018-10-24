package de.hatoka.player.internal.persistence;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface HistoryDao extends JpaRepository<HistoryPO, Long>
{
    List<HistoryPO> getByGameRef(String gameRef);
    List<HistoryPO> getByPlayerRef(String playerRef);
}
