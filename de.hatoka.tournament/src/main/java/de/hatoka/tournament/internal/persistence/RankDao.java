package de.hatoka.tournament.internal.persistence;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface RankDao extends JpaRepository<RankPO, Long>
{
    List<RankPO> getByTournamentID(Long tournamentID);
}
