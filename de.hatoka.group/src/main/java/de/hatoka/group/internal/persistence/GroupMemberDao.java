package de.hatoka.group.internal.persistence;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface GroupMemberDao extends JpaRepository<GroupMemberPO, Long>
{
    List<GroupMemberPO> findByPlayerRef(String playerRef);

    List<GroupMemberPO> findByGroupRef(String groupRef);
}
