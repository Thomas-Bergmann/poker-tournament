package de.hatoka.group.internal.persistence;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface GroupAdminDao extends JpaRepository<GroupAdminPO, Long>
{
    List<GroupAdminPO> findByUserRef(String userRef);

    List<GroupAdminPO> findByGroupRef(String groupRef);
}
