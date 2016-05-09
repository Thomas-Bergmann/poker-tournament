package de.hatoka.group.capi.business;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.Collection;

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
import de.hatoka.group.capi.business.GroupBO;
import de.hatoka.group.capi.business.GroupBORepository;
import de.hatoka.group.capi.business.GroupBusinessFactory;
import de.hatoka.group.capi.business.MemberBO;
import de.hatoka.group.modules.GroupBusinessModule;
import de.hatoka.group.modules.GroupDaoJpaModule;
import de.hatoka.test.DerbyEntityManagerRule;

public class GroupBOTest
{
    private static final String OWNER_REF = GroupBOTest.class.getSimpleName();

    @ClassRule
    public static DerbyEntityManagerRule rule = new DerbyEntityManagerRule("TestGroupPU");
    private static Injector injector = null;

    @Inject
    private GroupBusinessFactory factory;

    @Inject
    private TransactionProvider transactionProvider;

    private GroupBORepository groupBORepository;
    private GroupBO underTest;

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
        groupBORepository = factory.getGroupBORepository(OWNER_REF);
        transactionProvider.get().begin();
        underTest = groupBORepository.createGroup("testGroup", "ownerName");
        transactionProvider.get().commit();
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
        if (underTest != null)
        {
            underTest.remove();
        }
        transaction.commit();
    }

    @Test
    public void testStaticContent()
    {
        assertEquals("owner correct set", OWNER_REF, underTest.getOwner());
        assertEquals("owner correct set", "testGroup", underTest.getName());
    }

    @Test
    public void testCreateMember()
    {
        Collection<MemberBO> members = underTest.getMembers();
        assertEquals("owner correct set", 1, members.size());
        assertEquals("owner correct set", "ownerName", members.iterator().next().getName());

        // add new member
        EntityTransaction transaction = transactionProvider.get();
        transaction.begin();
        MemberBO member = underTest.createMember("userRef", "userName");
        transaction.commit();
        members = underTest.getMembers();
        assertEquals("two members", 2, members.size());
        assertTrue("contains new member", members.contains(member));
    }
}
