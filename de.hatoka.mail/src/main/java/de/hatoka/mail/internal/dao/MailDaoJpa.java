package de.hatoka.mail.internal.dao;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.List;

import javax.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.hatoka.common.capi.dao.EntityManagerProvider;
import de.hatoka.common.capi.dao.GenericJPADao;
import de.hatoka.common.capi.dao.UUIDGenerator;
import de.hatoka.common.capi.exceptions.MandatoryParameterException;
import de.hatoka.mail.capi.dao.MailDao;
import de.hatoka.mail.capi.dao.MessageIDGenerator;
import de.hatoka.mail.capi.entities.MailAttachmentPO;
import de.hatoka.mail.capi.entities.MailPO;
import de.hatoka.mail.capi.entities.MailReceiverPO;
import de.hatoka.mail.capi.entities.MailResultReceiverPO;
import de.hatoka.mail.capi.service.MailAddressType;
import de.hatoka.mail.capi.service.MailResultType;

public class MailDaoJpa extends GenericJPADao<MailPO> implements MailDao
{
    private static final Logger LOGGER = LoggerFactory.getLogger(MailDaoJpa.class);

    @Inject
    UUIDGenerator uuidGenerator;
    @Inject
    private MessageIDGenerator messageIdGenerator;
    @Inject
    private EntityManagerProvider entityManagerProvider;

    public MailDaoJpa()
    {
        super(MailPO.class);
    }

    @Override
    public MailPO createAndInsert(String accountRef)
    {
        if (accountRef == null)
        {
            throw new MandatoryParameterException("accountRef");
        }
        MailPO result = create();
        result.setId(messageIdGenerator.generate(accountRef));
        insert(result);
        return result;
    }

    @Override
    public MailAttachmentPO createAttachment(MailPO mail, File file) throws IOException
    {
        MailAttachmentPO result = new MailAttachmentPO();
        result.setId(uuidGenerator.generate());
        result.setMail(mail);
        result.setName(file.getName());
        result.setContent(Files.readAllBytes(file.toPath()));
        result.setMimeType(getMimeType(file));
        entityManagerProvider.get().persist(result);
        List<MailAttachmentPO> attachments = mail.getAttachments();
        attachments.add(result);
        mail.setAttachments(attachments);
        return result;
    }

    @Override
    public MailReceiverPO createReceiver(MailPO mail, MailAddressType type, String address)
    {
        MailReceiverPO result = new MailReceiverPO();
        result.setId(uuidGenerator.generate());
        result.setMail(mail);
        result.setType(type.getAddressType());
        result.setAddress(address);
        entityManagerProvider.get().persist(result);
        List<MailReceiverPO> receivers = mail.getReceivers();
        receivers.add(result);
        mail.setReceivers(receivers);
        return result;
    }

    @Override
    public MailResultReceiverPO createResult(MailReceiverPO receiver, MailResultType type)
    {
        MailResultReceiverPO result = new MailResultReceiverPO();
        result.setId(uuidGenerator.generate());
        result.setReceiver(receiver);
        result.setResult(type.getTypeBit());
        entityManagerProvider.get().persist(result);
        List<MailResultReceiverPO> results = receiver.getResults();
        results.add(result);
        receiver.setResults(results);
        return result;
    }

    private static String getMimeType(File file)
    {
        try
        {
            return Files.probeContentType(file.toPath());
        }
        catch(IOException e)
        {
            LOGGER.warn("Can't define mimetype for file:" + file.getName(), e);
        }
        return "application/unknown";
    }

}
