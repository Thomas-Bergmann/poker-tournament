package de.hatoka.tournament.internal.persistence;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

public interface TournamentDao extends JpaRepository<TournamentPO, Long>
{
    Optional<TournamentPO> findByOwnerRefAndLocalRef(String ownerRef, String localRef);

    List<TournamentPO> getByOwnerRef(String ownerRef);
}
