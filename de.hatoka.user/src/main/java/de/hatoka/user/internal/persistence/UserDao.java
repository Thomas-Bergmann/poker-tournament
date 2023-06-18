package de.hatoka.user.internal.persistence;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

public interface UserDao extends JpaRepository<UserPO, Long>
{
    /**
     * @param externalRef
     * @return user with the given login or external reference
     */
    public Optional<UserPO> findByGlobalRef(String externalRef);

}