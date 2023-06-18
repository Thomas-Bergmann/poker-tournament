package de.hatoka.tournament.internal.persistence;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface BlindLevelDao extends JpaRepository<BlindLevelPO, Long>
{
    List<BlindLevelPO> getByTournamentID(Long tournamentID);
}
