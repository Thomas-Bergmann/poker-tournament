package de.hatoka.mail.internal.dao;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;

import org.apache.derby.jdbc.EmbeddedDataSource;
import org.apache.naming.java.javaURLContextFactory;
import org.junit.rules.ExternalResource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.inject.Binder;
import com.google.inject.Module;

import de.hatoka.common.capi.dao.EntityManagerProvider;
import de.hatoka.common.capi.dao.TransactionProvider;
import de.hatoka.mail.capi.config.SmtpConfiguration;
import de.hatoka.mail.internal.service.SmtpConfigurationImpl;

public class DerbyEntityManagerRule extends ExternalResource implements EntityManagerProvider
{
    private static void initContext()
    {
        System.setProperty(Context.INITIAL_CONTEXT_FACTORY, javaURLContextFactory.class.getName());
        System.setProperty(Context.URL_PKG_PREFIXES, "org.apache.naming");

        try
        {
            InitialContext ic = new InitialContext();
            ic.createSubcontext("java:");
            ic.createSubcontext("java:comp");
            ic.createSubcontext("java:comp/env");
            ic.createSubcontext("java:comp/env/jdbc");

            EmbeddedDataSource ds = new EmbeddedDataSource();
            ds.setDatabaseName("build/derby-test-db");
            // tell Derby to create the database if it does not already exist
            ds.setCreateDatabase("create");
            ic.bind("java:comp/env/jdbc/mailTestDS", ds);
        }
        catch(NamingException e)
        {
            LOGGER.error("Can't bind test database.", e);
        }
    }

    private static final Logger LOGGER = LoggerFactory.getLogger(DerbyEntityManagerRule.class);
    static
    {
        initContext();
    }

    private EntityManagerFactory emf;
    private EntityManager em;

    @Override
    public void before() throws Throwable
    {
        super.before();
        emf = Persistence.createEntityManagerFactory("mailTestPU");
        em = emf.createEntityManager();
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
                binder.bind(SmtpConfiguration.class).to(SmtpConfigurationImpl.class).asEagerSingleton();
            }
        };
    }
}
