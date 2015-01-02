package de.hatoka.common.capi.modules;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;

import com.google.inject.Binder;
import com.google.inject.Module;

import de.hatoka.common.capi.dao.EntityManagerProvider;
import de.hatoka.common.capi.dao.JpaDB;
import de.hatoka.common.capi.dao.TransactionProvider;

public class JpaDBModule implements Module
{
    private final String persistenceUnitName;

    public JpaDBModule(String persistenceUnitName)
    {
        this.persistenceUnitName = persistenceUnitName;
    }

    @Override
    public void configure(Binder binder)
    {
        final JpaDB accountDB = new JpaDB(persistenceUnitName);
        binder.bind(EntityManagerProvider.class).toInstance(new EntityManagerProvider()
        {
            @Override
            public EntityManager get()
            {
                return accountDB.getEntityManager();
            }
        });
        binder.bind(TransactionProvider.class).toInstance(new TransactionProvider()
        {
            @Override
            public EntityTransaction get()
            {
                return accountDB.getEntityManager().getTransaction();
            }
        });
    }
}
