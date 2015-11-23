package de.hatoka.common.capi.dao;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

public class JpaDB
{
    private final EntityManagerFactory emf;
    private final EntityManager em;

    public JpaDB(String persistenceUnitName)
    {
        Map<String, Object> addedOrOverridenProperties = new HashMap<String, Object>();
        // Let's suppose we are using Hibernate as JPA provider
        addedOrOverridenProperties.put("hibernate.show_sql", true);
        for (Entry<String, String> systemEntry : System.getenv().entrySet())
        {
            if (systemEntry.getKey().startsWith("javax.persistence.jdbc"))
            {
                addedOrOverridenProperties.put(systemEntry.getKey(), systemEntry.getValue());
            }
        }

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
