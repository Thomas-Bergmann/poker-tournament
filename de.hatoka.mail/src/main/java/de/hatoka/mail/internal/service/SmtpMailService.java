package de.hatoka.mail.internal.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.inject.Inject;
import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.mail.util.ByteArrayDataSource;

import de.hatoka.mail.capi.config.SmtpConfiguration;
import de.hatoka.mail.capi.entities.MailAttachmentPO;
import de.hatoka.mail.capi.entities.MailPO;
import de.hatoka.mail.capi.entities.MailReceiverPO;
import de.hatoka.mail.capi.service.MailAddressType;
import de.hatoka.mail.capi.service.MailConstants;
import de.hatoka.mail.capi.service.MailResultType;
import de.hatoka.mail.capi.service.MailService;

/**
 * Implementation of a {@link MailService} via SMTP. This class requires a
 * {@link SmtpConfiguration}.
 */
public class SmtpMailService implements MailService
{
    @Inject
    private SmtpConfiguration configuration;

    /**
     * Add addresses of mail to mime message.
     *
     * @param mail
     * @param message
     * @throws MessagingException
     * @throws AddressException
     */
    private void addAddresses(MailPO mail, MimeMessage message) throws MessagingException, AddressException
    {
        Map<MailAddressType, List<InternetAddress>> addresses = getGroupedAddresses(mail);

        // ***set FROM address
        List<InternetAddress> addressesFrom = addresses.get(MailAddressType.FROM);
        if (addressesFrom != null)
        {
            message.setFrom(addressesFrom.get(0));
        }

        // ***set reply address
        List<InternetAddress> list = addresses.get(MailAddressType.REPLY);
        if (list == null)
        {
            list = addressesFrom;
        }
        message.setReplyTo(toArray(list));

        // *** set TO addresses
        list = addresses.get(MailAddressType.TO);
        message.setRecipients(Message.RecipientType.TO, toArray(list));

        // ***set CC if present
        list = addresses.get(MailAddressType.CC);
        if (list == null)
        {
            message.setRecipients(Message.RecipientType.CC, toArray(list));
        }

        // ***set BCC if present
        list = addresses.get(MailAddressType.BCC);
        if (list == null)
        {
            message.setRecipients(Message.RecipientType.BCC, toArray(list));
        }
    }

    /**
     * Add file attachments to multi part document
     *
     * @param multipart
     * @param attachments
     * @throws MessagingException
     */
    private void addFileAttachments(MimeMultipart multipart, List<MailAttachmentPO> attachments)
                    throws MessagingException
    {
        for (MailAttachmentPO attachment : attachments)
        {
            if (null == attachment.getCid() || attachment.getCid().isEmpty())
            {
                DataSource byteDataSource = new ByteArrayDataSource(attachment.getContent(), attachment.getMimeType());

                DataHandler attachementDataHandler = new DataHandler(byteDataSource);

                BodyPart memoryBodyPart = new MimeBodyPart();

                memoryBodyPart.setDataHandler(attachementDataHandler);

                memoryBodyPart.setFileName(attachment.getName());

                // Add part to multi-part
                multipart.addBodyPart(memoryBodyPart);
            }
        }
    }

    /**
     * Add inline attachment (like images of html message)
     *
     * @param encoding
     * @param messageBodyPart
     * @param content
     * @param isHtml
     * @param attachments
     * @return
     * @throws MessagingException
     */
    private MimeMultipart addInlineAttachments(String encoding, MimeBodyPart messageBodyPart, String content,
                    boolean isHtml, List<MailAttachmentPO> attachments) throws MessagingException
    {
        List<MailAttachmentPO> inlineAttachmentList = new ArrayList<MailAttachmentPO>();
        for (MailAttachmentPO attachment : attachments)
        {
            if (attachment.getCid() != null && !attachment.getCid().isEmpty())
            {
                inlineAttachmentList.add(attachment);
            }
        }

        MimeMultipart multipart = null;

        if (!inlineAttachmentList.isEmpty() || isHtml)
        {
            multipart = new MimeMultipart("related");

            if (null != encoding && !encoding.isEmpty())
            {
                messageBodyPart.setText(content, encoding);
                messageBodyPart.setHeader("Content-Type", "text/html; charset=\"" + encoding + "\"");
                messageBodyPart.setHeader("Content-Transfer-Encoding", "quoted-printable");
            }
            else
            {
                messageBodyPart.setContent(content, "text/html");
            }

            multipart.addBodyPart(messageBodyPart);

            for (MailAttachmentPO attachment : inlineAttachmentList)
            {
                String cid = attachment.getCid();

                MimeBodyPart inlineBodyPart = new MimeBodyPart();

                // Fetch the image and associate to part
                DataSource byteDataSource = new ByteArrayDataSource(attachment.getContent(), attachment.getMimeType());

                inlineBodyPart.setDataHandler(new DataHandler(byteDataSource));

                // Add a header to connect to the HTML
                inlineBodyPart.setHeader("Content-ID", cid);
                // Add part to multi-part
                multipart.addBodyPart(inlineBodyPart);
            }
        }
        else
        {
            // no inline attachments
            multipart = new MimeMultipart();
            if (null != encoding && !encoding.isEmpty())
            {
                messageBodyPart.setText(content, encoding);
            }
            else
            {
                messageBodyPart.setText(content);
            }
            multipart.addBodyPart(messageBodyPart);
        }
        return multipart;
    }

    private void addMessageID(MailPO mail, MimeMessage message) throws MessagingException
    {
        message.setHeader(MailConstants.KEY_MESSAGE_ID_HEADER, mail.getId());
    }

    /**
     * Group addresses by type
     *
     * @param mailPO
     * @return
     */
    private Map<MailAddressType, List<InternetAddress>> getGroupedAddresses(MailPO mailPO)
    {
        Map<MailAddressType, List<InternetAddress>> result = new HashMap<>();
        for (MailReceiverPO receiverPO : mailPO.getReceivers())
        {
            MailAddressType type = MailAddressType.getBy(receiverPO.getType());
            List<InternetAddress> someType = result.get(type);
            if (someType == null)
            {
                someType = new ArrayList<>();
                result.put(type, someType);
            }
            try
            {
                someType.add(new InternetAddress(receiverPO.getAddress()));
            }
            catch(AddressException exception)
            {
                throw new RuntimeException(exception);
            }
        }
        return result;
    }

    /**
     * Creates a mime message from mail entity.
     *
     * @param session
     * @param mail
     * @return
     * @throws AddressException
     * @throws MessagingException
     */
    public Message getMessage(Session session, MailPO mail) throws AddressException, MessagingException
    {
        MimeMessage message = new MimeMessage(session);
        addMessageID(mail, message);

        addAddresses(mail, message);

        setSubject(mail, message);

        // *** Create the message part
        MimeBodyPart messageBodyPart = new MimeBodyPart();

        // 0.if plain text, use setText otherwise attachment sign appears in
        // Outlook
        String content = mail.getContent();
        String htmlContent = mail.getContentHTML();
        boolean isHtml = htmlContent != null && !htmlContent.isEmpty();
        if (isHtml)
        {
            content = htmlContent;
        }
        List<MailAttachmentPO> attachments = mail.getAttachments();
        String encoding = mail.getEncoding();
        // simple text no attachments
        if (attachments.isEmpty() && !isHtml)
        {
            if (null != encoding && !encoding.isEmpty())
            {
                message.setText(content, encoding);
            }
            else
            {
                message.setText(content);
            }
        }
        else
        {
            MimeMultipart multipart = addInlineAttachments(encoding, messageBodyPart, content, isHtml, attachments);

            addFileAttachments(multipart, attachments);

            message.setContent(multipart);
        }
        return message;

    }

    @Override
    public MailResultType sendMail(MailPO mailPO)
    {
        Session session = configuration.getSession();
        try
        {
            Message message = getMessage(session, mailPO);
            message.setSentDate(new Date());
            Transport.send(message);
        }
        catch(Exception e)
        {
            throw new RuntimeException("Can't send message", e);
        }
        return MailResultType.OK;
    }

    /**
     * set the mail subject line with the appropriate encoding the application
     * must ensure that the subject does not contain any line breaks.
     *
     * @param mail
     * @param message
     * @throws MessagingException
     */
    private void setSubject(MailPO mail, MimeMessage message) throws MessagingException
    {
        String encoding = mail.getEncoding();
        String subject = mail.getSubject();
        if (null != encoding && !encoding.isEmpty())
        {
            message.setSubject(subject, encoding);
        }
        else
        {
            message.setSubject(subject);
        }
    }

    /**
     * Converts a list of addresses to an array.
     *
     * @param list
     * @return
     */
    private InternetAddress[] toArray(List<InternetAddress> list)
    {
        if (list == null)
        {
            return new InternetAddress[0];
        }
        return list.toArray(new InternetAddress[list.size()]);
    }

}
