package de.hatoka.player.internal.persistence;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

public interface PlayerDao extends JpaRepository<PlayerPO, Long>
{
    public Optional<PlayerPO> findByContextRefAndPlayerRef(String contextRef, String localRef);

    public List<PlayerPO> getByContextRef(String contextRef);
}