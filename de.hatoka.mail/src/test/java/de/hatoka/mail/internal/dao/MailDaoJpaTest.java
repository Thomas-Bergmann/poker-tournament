package de.hatoka.mail.internal.dao;

import static org.junit.Assert.assertEquals;

import java.io.File;
import java.io.IOException;

import javax.inject.Inject;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import com.google.inject.Guice;
import com.google.inject.Injector;

import de.hatoka.common.capi.modules.CommonDaoModule;
import de.hatoka.mail.capi.dao.MailDao;
import de.hatoka.mail.capi.entities.MailPO;
import de.hatoka.mail.capi.entities.MailReceiverPO;
import de.hatoka.mail.capi.service.MailAddressType;
import de.hatoka.mail.capi.service.MailResultType;
import de.hatoka.mail.internal.modules.MailDaoJpaModule;
import de.hatoka.test.DerbyEntityManagerRule;

public class MailDaoJpaTest
{
    @Rule
    public DerbyEntityManagerRule rule = new DerbyEntityManagerRule();

    @Inject
    private MailDao mailDao;

    @Before
    public void createTestObject()
    {
        Injector injector = Guice.createInjector(new CommonDaoModule(), new MailDaoJpaModule(), rule.getModule());
        injector.injectMembers(this);
    }

    @Test
    public void testMailCreation() throws IOException
    {
        MailPO mail = mailDao.createAndInsert("TestAccount");
        mail.setSubject("SimpleSubject");
        mail.setContent("SimpleText");
        MailReceiverPO receiver = mailDao.createReceiver(mail, MailAddressType.FROM, "testFrom@test.test");
        mailDao.createReceiver(mail, MailAddressType.TO, "testTo@test.test");
        File file = File.createTempFile("testMailAttachmentCreation",".xml");
        mailDao.createAttachment(mail, file);
        mailDao.createResult(receiver, MailResultType.NOT_SEND);

        assertEquals("number of receivers", 2, mail.getReceivers().size());
        MailReceiverPO first = mail.getReceivers().get(0);
        assertEquals("first address", "testFrom@test.test", first.getAddress());
        assertEquals("first type", MailAddressType.FROM.getAddressType(), first.getType());
        MailReceiverPO second = mail.getReceivers().get(1);
        assertEquals("second address", "testTo@test.test", second.getAddress());
        assertEquals("second type", MailAddressType.TO.getAddressType(), second.getType());

    }
}
