package de.hatoka.test;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;

import org.junit.rules.ExternalResource;

import com.google.inject.Binder;
import com.google.inject.Module;

import de.hatoka.common.capi.dao.EntityManagerProvider;
import de.hatoka.common.capi.dao.TransactionProvider;

public class DerbyEntityManagerRule extends ExternalResource implements EntityManagerProvider
{
    private EntityManagerFactory emf;
    private EntityManager em;
    private final String persistentUnitName;

    public DerbyEntityManagerRule(String persistentUnitName)
    {
        this.persistentUnitName = persistentUnitName;
    }

    @Override
    public void before() throws Throwable
    {
        super.before();
        emf = Persistence.createEntityManagerFactory(persistentUnitName);
        em = emf.createEntityManager();
    }

    @Override
    public void after()
    {
        close();
        super.after();
    }

    public void close()
    {
        if (emf.isOpen())
        {
            emf.close();
        }
    }

    @Override
    public EntityManager get()
    {
        return em;
    }

    public Module getModule()
    {
        final EntityManagerProvider emp = this;
        return new Module()
        {
            @Override
            public void configure(Binder binder)
            {
                binder.bind(EntityManagerProvider.class).toInstance(emp);
                binder.bind(TransactionProvider.class).toInstance(new TransactionProvider()
                {

                    @Override
                    public EntityTransaction get()
                    {
                        return emp.get().getTransaction();
                    }
                });
            }
        };
    }
}
