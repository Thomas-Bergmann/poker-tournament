package de.hatoka.cashgame.internal.persistence;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface CashGameCompetitorDao extends JpaRepository<CashGameCompetitorPO, Long>
{
    List<CashGameCompetitorPO> getByCashGameID(Long cashGameID);
}
