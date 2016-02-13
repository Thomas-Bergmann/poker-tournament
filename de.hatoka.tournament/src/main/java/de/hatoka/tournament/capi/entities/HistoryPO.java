package de.hatoka.tournament.capi.entities;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.AttributeOverride;
import javax.persistence.AttributeOverrides;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlTransient;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import org.eclipse.persistence.oxm.annotations.XmlInverseReference;

import de.hatoka.common.capi.app.xslt.DateXmlAdapter;
import de.hatoka.common.capi.dao.IdentifiableEntity;
import de.hatoka.common.capi.entities.MoneyPO;

@Entity
public class HistoryPO implements Serializable, IdentifiableEntity
{
    private static final long serialVersionUID = 1L;

    @Id
    @XmlTransient
    private String id;

    @NotNull
    @Column(updatable = false)
    @XmlTransient
    private String accountRef;

    @NotNull
    @XmlAttribute(name="player")
    private String player;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "tournament", updatable = false)
    @XmlInverseReference(mappedBy="historyEntries")
    private TournamentPO tournament;

    /**
     * The player is in play
     */
    @XmlAttribute
    private String actionKey;

    /**
     * The player is out and was placed at position
     */
    @NotNull
    @Temporal(TemporalType.TIMESTAMP)
    @XmlAttribute
    @XmlJavaTypeAdapter(DateXmlAdapter.class)
    private Date date;

    @Embedded
    @AttributeOverrides({ @AttributeOverride(name = "currencyCode", column = @Column(name = "amountCur")),
        @AttributeOverride(name = "amount", column = @Column(name = "amount")) })
    @XmlElement(name="amount")
    private MoneyPO amount;

    public HistoryPO()
    {

    }

    @Override
    public boolean equals(Object obj)
    {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        HistoryPO other = (HistoryPO)obj;
        if (id == null)
        {
            if (other.id != null)
                return false;
        }
        else if (!id.equals(other.id))
            return false;
        return true;
    }

    @XmlTransient
    public String getAccountRef()
    {
        return accountRef;
    }

    @Override
    @XmlTransient
    public String getId()
    {
        return id;
    }

    @XmlTransient
    public MoneyPO getAmount()
    {
        return amount;
    }

    @XmlTransient
    public String getPlayer()
    {
        return player;
    }

    @XmlTransient
    public TournamentPO getTournamentPO()
    {
        return tournament;
    }

    @Override
    public int hashCode()
    {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((id == null) ? 0 : id.hashCode());
        return result;
    }

    public void setAccountRef(String accountRef)
    {
        this.accountRef = accountRef;
    }

    @Override
    public void setId(String id)
    {
        this.id = id;
    }

    public void setAmount(MoneyPO moneyAmount)
    {
        this.amount = moneyAmount;
    }

    public void setPlayer(String player)
    {
        this.player = player;
    }

    public void setTournamentPO(TournamentPO tournamentPO)
    {
        this.tournament = tournamentPO;
    }

    @XmlTransient
    public String getActionKey()
    {
        return actionKey;
    }

    public void setActionKey(String actionKey)
    {
        this.actionKey = actionKey;
    }

    @XmlTransient
    public Date getDate()
    {
        return date;
    }

    public void setDate(Date date)
    {
        this.date = date;
    }
}
