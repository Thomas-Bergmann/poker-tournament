package de.hatoka.mail.capi.entities;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;

import de.hatoka.mail.capi.service.MailResultType;

@Entity
public class MailResultReceiverPO implements Serializable
{
    /**
     * generated
     */
    private static final long serialVersionUID = 1356036426723792362L;

    @Id
    private String id;

    /**
     * exception that has occurred during the sending process.
     */
    @Lob
    private Exception exception;

    /**
     * all addresses for which a result is available
     */
    @ManyToOne
    @JoinColumn(name = "receiverRef", updatable = false)
    private MailReceiverPO receiver;

    /**
     * result of the single address, see {@link MailResultType}
     */
    private int result;

    public Exception getException()
    {
        return exception;
    }

    public String getId()
    {
        return id;
    }

    public MailReceiverPO getReceiver()
    {
        return receiver;
    }

    public int getResult()
    {
        return result;
    }

    public void setException(Exception exception)
    {
        this.exception = exception;
    }

    public void setId(String id)
    {
        this.id = id;
    }

    public void setReceiver(MailReceiverPO receiver)
    {
        this.receiver = receiver;
    }

    public void setResult(int result)
    {
        this.result = result;
    }
}
