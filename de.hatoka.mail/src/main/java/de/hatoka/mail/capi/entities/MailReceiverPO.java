package de.hatoka.mail.capi.entities;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;

import de.hatoka.mail.capi.service.MailAddressType;
import de.hatoka.mail.capi.service.MailResultType;

/**
 * MailReceiver receives a mail.
 */
@Entity
public class MailReceiverPO implements Serializable
{
    /**
     * generated
     */
    private static final long serialVersionUID = 6978304803625672116L;

    @Id
    private String id;

    @ManyToOne
    @JoinColumn(name = "mailRef", updatable = false)
    private MailPO mail;

    /**
     * results for receivers (multiple if temporary failed)
     */
    @OneToMany(mappedBy = "receiver")
    private List<MailResultReceiverPO> results = new ArrayList<>();

    /**
     * email address like "Max Mustermann" &lt;max.mustermann@test.test&gt;.
     */
    @NotNull
    private String address;

    /**
     * last transport date
     */
    @Temporal(TemporalType.DATE)
    private Date transportDate;

    /**
     * last result type, see {@link MailResultType}
     */
    private int resultType;

    /**
     * type of address, see {@link MailAddressType}
     */
    private int type;

    public String getAddress()
    {
        return address;
    }

    public String getId()
    {
        return id;
    }

    public MailPO getMail()
    {
        return mail;
    }

    public List<MailResultReceiverPO> getResults()
    {
        return results;
    }

    public int getResultType()
    {
        return resultType;
    }

    public Date getTransportDate()
    {
        return transportDate;
    }

    public int getType()
    {
        return type;
    }

    public void setAddress(String address)
    {
        this.address = address;
    }

    public void setId(String id)
    {
        this.id = id;
    }

    public void setMail(MailPO mail)
    {
        this.mail = mail;
    }

    public void setResults(List<MailResultReceiverPO> results)
    {
        this.results = results;
    }

    public void setResultType(int resultType)
    {
        this.resultType = resultType;
    }

    public void setTransportDate(Date transportDate)
    {
        this.transportDate = transportDate;
    }

    public void setType(int type)
    {
        this.type = type;
    }
}
