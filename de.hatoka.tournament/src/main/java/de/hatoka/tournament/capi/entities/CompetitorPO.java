package de.hatoka.tournament.capi.entities;

import java.io.Serializable;

import javax.persistence.AttributeOverride;
import javax.persistence.AttributeOverrides;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlIDREF;
import javax.xml.bind.annotation.XmlTransient;

import de.hatoka.common.capi.dao.IdentifiableEntity;
import de.hatoka.common.capi.entities.MoneyPO;
import de.hatoka.tournament.capi.types.CompetitorState;

@Entity
public class CompetitorPO implements Serializable, IdentifiableEntity
{
    private static final long serialVersionUID = 1L;

    @Id
    @XmlTransient
    private String id;

    @ManyToOne
    @JoinColumn(name = "playerRef", updatable = true)
    @XmlIDREF
    @XmlAttribute(name="playerRef")
    private PlayerPO playerRef;

    @NotNull
    @XmlAttribute(name="player")
    private String player;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "tournament", updatable = false)
    @XmlTransient
    private TournamentPO tournament;

    /**
     * The player is out and was placed at position
     */
    @XmlAttribute
    private Integer position;

    @Embedded
    @AttributeOverrides({ @AttributeOverride(name = "currencyCode", column = @Column(name = "inplayCur")),
                    @AttributeOverride(name = "amount", column = @Column(name = "inplayAmount")) })
    @XmlElement(name="inplay")
    private MoneyPO moneyInPlay;

    @Embedded
    @AttributeOverrides({ @AttributeOverride(name = "currencyCode", column = @Column(name = "resultCur")),
                    @AttributeOverride(name = "amount", column = @Column(name = "resultAmount")) })
    @XmlElement(name="result")
    private MoneyPO moneyResult;

    /**
     * state of player see {@link CompetitorState}
     */
    @NotNull
    @XmlAttribute
    private String state;

    /**
     * The player seats on table
     */
    @XmlAttribute
    private int tableNo = -1;

    /**
     * The player seats at seat on table
     */
    @XmlAttribute
    private int seatNo = -1;


    public CompetitorPO()
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
        CompetitorPO other = (CompetitorPO)obj;
        if (id == null)
        {
            if (other.id != null)
                return false;
        }
        else if (!id.equals(other.id))
            return false;
        return true;
    }

    @Override
    @XmlTransient
    public String getId()
    {
        return id;
    }

    @XmlTransient
    public MoneyPO getMoneyInPlay()
    {
        return moneyInPlay;
    }

    @XmlTransient
    public MoneyPO getMoneyResult()
    {
        return moneyResult;
    }

    @XmlTransient
    public PlayerPO getPlayerPO()
    {
        return playerRef;
    }

    @XmlTransient
    public Integer getPosition()
    {
        return position;
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

    @Override
    public void setId(String id)
    {
        this.id = id;
    }

    public void setMoneyInPlay(MoneyPO moneyInPlay)
    {
        this.moneyInPlay = moneyInPlay;
    }

    public void setMoneyResult(MoneyPO moneyResult)
    {
        this.moneyResult = moneyResult;
    }

    public void setPlayerPO(PlayerPO playerPO)
    {
        this.playerRef = playerPO;
    }

    public void setPosition(Integer position)
    {
        this.position = position;
    }

    public void setTournamentPO(TournamentPO tournamentPO)
    {
        tournament = tournamentPO;
    }

    @XmlTransient
    public String getState()
    {
        return state;
    }

    public void setState(String state)
    {
        this.state = state;
    }

    @XmlTransient
    public int getTableNo()
    {
        return tableNo;
    }

    public void setTableNo(int tableNo)
    {
        this.tableNo = tableNo;
    }

    @XmlTransient
    public int getSeatNo()
    {
        return seatNo;
    }

    public void setSeatNo(int seatNo)
    {
        this.seatNo = seatNo;
    }

    @XmlTransient
    public String getPlayer()
    {
        return player;
    }

    public void setPlayer(String player)
    {
        this.player = player;
    }
}
