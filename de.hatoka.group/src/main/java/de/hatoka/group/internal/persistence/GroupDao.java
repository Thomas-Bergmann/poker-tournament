package de.hatoka.group.internal.persistence;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

public interface GroupDao extends JpaRepository<GroupPO, Long>
{
    Optional<GroupPO> findByGlobalGroupRef(String globalGroupRef);
}
