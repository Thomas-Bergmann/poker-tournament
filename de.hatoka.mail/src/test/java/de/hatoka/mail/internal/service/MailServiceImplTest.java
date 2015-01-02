package de.hatoka.mail.internal.service;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;

import javax.inject.Inject;

import org.apache.commons.io.IOUtils;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import com.google.inject.Guice;
import com.google.inject.Injector;

import de.hatoka.common.capi.modules.CommonDaoModule;
import de.hatoka.mail.capi.dao.MailDao;
import de.hatoka.mail.capi.entities.MailPO;
import de.hatoka.mail.capi.service.MailAddressType;
import de.hatoka.mail.capi.service.MailService;
import de.hatoka.mail.internal.dao.DerbyEntityManagerRule;
import de.hatoka.mail.internal.modules.MailDaoJpaModule;
import de.hatoka.mail.internal.modules.MailServiceModule;

public class MailServiceImplTest
{
    private static final String RESOURCE_PREFIX = "de/hatoka/mail/internal/service/";
    @Rule
    public DerbyEntityManagerRule rule = new DerbyEntityManagerRule();

    @Inject
    private MailDao mailDao;

    @Inject
    private MailService UNDER_TEST;

    @Before
    public void createTestObject()
    {
        Injector injector = Guice.createInjector(new MailServiceModule(), new CommonDaoModule(),
                        new MailDaoJpaModule(), rule.getModule());
        injector.injectMembers(this);
    }

    private String getResource(String string) throws IOException
    {
        StringWriter writer = new StringWriter();
        InputStream resourceAsStream = getClass().getClassLoader().getResourceAsStream(RESOURCE_PREFIX + string);
        if (resourceAsStream == null)
        {
            throw new FileNotFoundException("Can't find resource: " + RESOURCE_PREFIX + string);
        }
        IOUtils.copy(resourceAsStream, writer, "UTF-8");
        return writer.toString();
    }

    @Test
    public void testSendHtmlMail() throws IOException
    {
        MailPO mail = mailDao.createAndInsert("TestAccount");
        mail.setSubject("HtmlSubject");
        mail.setContentHTML(getResource("mailServiceHtml.resource"));
        mailDao.createReceiver(mail, MailAddressType.TO, "offlinepoker@hatoka.de");
        mailDao.createReceiver(mail, MailAddressType.FROM, "offlinepoker@hatoka.de");
        UNDER_TEST.sendMail(mail);
    }

    @Test
    public void testSendTextMail() throws IOException
    {
        MailPO mail = mailDao.createAndInsert("TestAccount");
        mail.setSubject("SimpleSubject");
        mail.setContent("SimpleText");
        mailDao.createReceiver(mail, MailAddressType.TO, "offlinepoker@hatoka.de");
        mailDao.createReceiver(mail, MailAddressType.FROM, "offlinepoker@hatoka.de");
        UNDER_TEST.sendMail(mail);
    }
}
