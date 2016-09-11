package de.hatoka.group.capi.business;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import javax.inject.Inject;
import javax.persistence.EntityTransaction;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.ClassRule;
import org.junit.Test;

import com.google.inject.Guice;
import com.google.inject.Injector;

import de.hatoka.common.capi.dao.TransactionProvider;
import de.hatoka.common.capi.modules.CommonDaoModule;
import de.hatoka.group.modules.GroupBusinessModule;
import de.hatoka.group.modules.GroupDaoJpaModule;
import de.hatoka.test.DerbyEntityManagerRule;

public class GroupBusinessFactoryTest
{
    private static final String OWNER_REF_0 = GroupBusinessFactoryTest.class.getSimpleName()+ "_0";
    private static final String OWNER_REF_1 = GroupBusinessFactoryTest.class.getSimpleName()+ "_1";

    @ClassRule
    public static DerbyEntityManagerRule rule = new DerbyEntityManagerRule("TestGroupPU");
    private static Injector injector = null;

    @Inject
    private GroupBusinessFactory factory;

    @Inject
    private TransactionProvider transactionProvider;

    private GroupBORepository repository0;
    private GroupBORepository repository1;

    @BeforeClass
    public static void createInjector()
    {
        injector = Guice.createInjector(new CommonDaoModule(100), new GroupDaoJpaModule(), new GroupBusinessModule(), rule.getModule());
    }

    @AfterClass
    public static void deleteInjector()
    {
        injector = null;
    }

    @Before
    public void createTestObject()
    {
        injector.injectMembers(this);
        repository0 = factory.getGroupBORepository(OWNER_REF_0);
        repository1 = factory.getGroupBORepository(OWNER_REF_1);
    }

    @After
    public void destroyCreatedObjects()
    {
        if (transactionProvider == null)
        {
            return;
        }
        EntityTransaction transaction = transactionProvider.get();
        if (transaction.isActive())
        {
            transaction.rollback();
        }
        transaction.begin();
        if (repository0 != null)
        {
            repository0.clear();
        }
        if (repository1 != null)
        {
            repository1.clear();
        }
        transaction.commit();
    }

    @Test
    public void testMembershipOfUser()
    {
        transactionProvider.get().begin();
        GroupBO group0 = repository0.createGroup("group0", "user0");
        GroupBO group1 = repository1.createGroup("group1", "user1");
        group1.createMember(OWNER_REF_0, "user0");
        assertEquals("group of user0 not changed", 1, group0.getMembers().size());
        assertEquals("group of user1 has owner and one member", 2, group1.getMembers().size());
        assertEquals("group of user0 is assigned to two groups", 2, factory.getGroupBOsByUser(OWNER_REF_0).size());
        assertEquals("group of user1 is still in one groups", 1, factory.getGroupBOsByUser(OWNER_REF_1).size());
        transactionProvider.get().rollback();
    }
    @Test
    public void testFindGroup()
    {
        transactionProvider.get().begin();
        GroupBO group0 = repository0.createGroup("group0", "user0");
        repository1.createGroup("group1", "user1");
        transactionProvider.get().commit();
        assertEquals("found group", group0, factory.findGroupBOByName("group0"));
        assertNull("cant found group", factory.findGroupBOByName("group2"));
    }
}
