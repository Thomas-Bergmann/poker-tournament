package de.hatoka.player.internal.persistence;

import java.io.Serializable;
import java.util.Date;
import java.util.Objects;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import javax.validation.constraints.NotNull;

@Entity
@Table(
    name = "players",
    uniqueConstraints={ @UniqueConstraint(columnNames= { "context_ref", "local_ref" }) }
)
public class PlayerPO implements Serializable
{
    private static final long serialVersionUID = 1L;

    /**
     * Internal identifier for persistence only
     */
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "player_id")
    private Long id;

    /**
     * User (knows player) user:&lt;userRef&gt;
     * Tournament (registered player) tournament:&lt;userRef&gt;
     */
    @NotNull
    @Column(name = "context_ref", nullable = false)
    private String contextRef;

    @NotNull
    @Column(name = "local_ref", nullable = false)
    private String playerRef;

    @NotNull
    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "email")
    private String eMail;

    @Column(name = "date_first")
    private Date firstDate;

    @Override
    public int hashCode()
    {
        return Objects.hash(contextRef, playerRef);
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
        PlayerPO other = (PlayerPO)obj;
        return Objects.equals(contextRef, other.contextRef) && Objects.equals(playerRef, other.playerRef);
    }

    public PlayerPO()
    {
    }

    public Long getId()
    {
        return id;
    }

    public String getContextRef()
    {
        return contextRef;
    }

    public void setContextRef(String contextRef)
    {
        this.contextRef = contextRef;
    }

    public String getName()
    {
        return name;
    }

    public void setId(Long id)
    {
        this.id = id;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public String getEMail()
    {
        return eMail;
    }

    public void setEMail(String eMail)
    {
        this.eMail = eMail;
    }

    public String getPlayerRef()
    {
        return playerRef;
    }

    public void setPlayerRef(String ref)
    {
        this.playerRef = ref;
    }

    public Date getFirstDate()
    {
        return firstDate;
    }

    public void setFirstDate(Date firstDate)
    {
        this.firstDate = firstDate;
    }

}
