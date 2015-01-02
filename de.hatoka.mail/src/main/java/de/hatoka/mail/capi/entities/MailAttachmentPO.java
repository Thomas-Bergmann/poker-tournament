package de.hatoka.mail.capi.entities;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

/**
 * MailAttachment can be attached to a mail, an attachment can be internal or
 * external.
 */
@Entity
public class MailAttachmentPO implements Serializable
{
    /**
     * generated
     */
    private static final long serialVersionUID = -7263248953996957444L;

    @Id
    private String id;

    @ManyToOne
    @JoinColumn(name = "mailRef", updatable = false)
    private MailPO mail;

    /**
     * Content identifier, used as marker inline of a multipart mail
     */
    private String cid;

    /**
     * content,depending on type can be binary or text
     */
    private byte[] content;

    /**
     * type of content
     */
    private String mimeType;

    /**
     * name of attachment, for outline (file attachments)
     */
    private String name;

    public String getCid()
    {
        return cid;
    }

    public byte[] getContent()
    {
        return content;
    }

    public MailPO getMail()
    {
        return mail;
    }

    public String getMimeType()
    {
        return mimeType;
    }

    public String getName()
    {
        return name;
    }

    public void setCid(String cid)
    {
        this.cid = cid;
    }

    public void setContent(byte[] content)
    {
        this.content = content;
    }

    public void setMail(MailPO mail)
    {
        this.mail = mail;
    }

    public void setMimeType(String mimeType)
    {
        this.mimeType = mimeType;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public String getId()
    {
        return id;
    }

    public void setId(String id)
    {
        this.id = id;
    }
}
