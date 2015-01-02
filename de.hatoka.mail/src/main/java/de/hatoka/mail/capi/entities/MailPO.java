package de.hatoka.mail.capi.entities;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.OneToMany;

import de.hatoka.common.capi.dao.IdentifiableEntity;

@Entity
public class MailPO implements Serializable, IdentifiableEntity
{
    /**
     * generated
     */
    private static final long serialVersionUID = 1655099241857256112L;

    @Id
    private String id;

    /**
     * addresses or receivers (to, bcc, from, ...)
     */
    @OneToMany(mappedBy = "mail")
    private List<MailReceiverPO> receivers = new ArrayList<>();

    /**
     * attachments inline and file attachments
     */
    @OneToMany(mappedBy = "mail")
    private List<MailAttachmentPO> attachments = new ArrayList<>();

    /**
     * headline of mail
     */
    private String subject;

    /**
     * text content
     */
    @Lob
    private String content;

    /**
     * html text content
     */
    @Lob
    private String contentHTML;

    /**
     * html or text content (what for both) TODO
     */
    private String contentType;

    /**
     * Encoding of text
     */
    private String encoding = "UTF-8";

    /**
     * date application creates the mail
     */
    private Date transportInitialized;

    /**
     * date application starts sending
     */
    private Date transportBegin;

    /**
     * date application finish sending
     */
    private Date transportEnd;

    /**
     * The results from the single addresses are aggregated with a binary or
     * operation * when no mails are sent, the result is RESULT_NOT_SEND * when
     * all mails are sent ok, the result is RESULT_OK * when some mails are OK,
     * and some have temporary errors, the result is RESULT_OK |
     * RESULT_ERROR_TEMPORARY * when all mails have temporary errors, the result
     * is RESULT_ERROR_TEMPORARY * when some mails are OK or have temporary
     * errors, and some mails have permanent errors, the result is RESULT_OK |
     * RESULT_ERROR_TEMPORARY | RESULT_ERROR_PERMANENT * when all mails have
     * permanent errors, the result is RESULT_ERROR_PERMANENT When a general
     * error has happened during mail send, the result is set to
     * RESULT_ERROR_GENERAL
     *
     * @return the aggregated result
     */
    private int aggregatedResult;

    public int getAggregatedResult()
    {
        return aggregatedResult;
    }

    public List<MailAttachmentPO> getAttachments()
    {
        return attachments;
    }

    public String getContent()
    {
        return content;
    }

    public String getContentHTML()
    {
        return contentHTML;
    }

    public String getContentType()
    {
        return contentType;
    }

    public String getEncoding()
    {
        return encoding;
    }

    @Override
    public String getId()
    {
        return id;
    }

    public List<MailReceiverPO> getReceivers()
    {
        return receivers;
    }

    public String getSubject()
    {
        return subject;
    }

    public Date getTransportBegin()
    {
        return transportBegin;
    }

    public Date getTransportEnd()
    {
        return transportEnd;
    }

    public Date getTransportInitialized()
    {
        return transportInitialized;
    }

    public void setAggregatedResult(int aggregatedResult)
    {
        this.aggregatedResult = aggregatedResult;
    }

    public void setAttachments(List<MailAttachmentPO> attachments)
    {
        this.attachments = attachments;
    }

    public void setContent(String content)
    {
        this.content = content;
    }

    public void setContentHTML(String contentHTML)
    {
        this.contentHTML = contentHTML;
    }

    public void setContentType(String contentType)
    {
        this.contentType = contentType;
    }

    public void setEncoding(String encoding)
    {
        this.encoding = encoding;
    }

    @Override
    public void setId(String id)
    {
        this.id = id;
    }

    public void setReceivers(List<MailReceiverPO> receivers)
    {
        this.receivers = receivers;
    }

    public void setSubject(String subject)
    {
        this.subject = subject;
    }

    public void setTransportBegin(Date transportBegin)
    {
        this.transportBegin = transportBegin;
    }

    public void setTransportEnd(Date transportEnd)
    {
        this.transportEnd = transportEnd;
    }

    public void setTransportInitialized(Date transportInitialized)
    {
        this.transportInitialized = transportInitialized;
    }
}
