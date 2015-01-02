package de.hatoka.common.capi.dao;

import javax.persistence.EntityManager;

public interface EntityManagerProvider
{
    EntityManager get();
}
