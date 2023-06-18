package de.hatoka.tournament.internal.persistence;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface CompetitorDao extends JpaRepository<CompetitorPO, Long>
{
    List<CompetitorPO> getByTournamentID(Long tournamentID);
}
