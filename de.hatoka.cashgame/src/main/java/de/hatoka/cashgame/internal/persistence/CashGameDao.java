package de.hatoka.cashgame.internal.persistence;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

public interface CashGameDao extends JpaRepository<CashGamePO, Long>
{
    Optional<CashGamePO> findByOwnerRefAndLocalRef(String ownerRef, String localRef);

    List<CashGamePO> getByOwnerRef(String ownerRef);
}
