package de.hatoka.common.capi.dao;

import java.util.HashMap;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

public class JpaDB
{
    private final EntityManagerFactory emf;
    private final EntityManager em;

    public JpaDB(String persistenceUnitName)
    {
        Map<String, Object> addedOrOverridenProperties = new HashMap<>();
        // Let's suppose we are using Hibernate as JPA provider
        addedOrOverridenProperties.put("hibernate.show_sql", true);

        emf = Persistence.createEntityManagerFactory(persistenceUnitName);
        em = emf.createEntityManager();
    }

    @Override
    public void finalize() throws Throwable
    {
        if (emf.isOpen())
        {
            emf.close();
        }
        super.finalize();
    }

    public EntityManager getEntityManager()
    {
        return em;
    }
}
