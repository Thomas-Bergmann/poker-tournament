package de.hatoka.group.internal.persistence;

import java.io.Serializable;
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
    name="group_member",
    uniqueConstraints={ @UniqueConstraint(columnNames= {"group_ref", "player_ref"}) }
)
public class GroupMemberPO implements Serializable
{
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "groupmember_id")
    private Long internalId;

    @NotNull
    @Column(name = "group_ref", nullable = false)
    private String groupRef;

    /**
     * player reference of member
     */
    @NotNull
    @Column(name = "player_ref", nullable = false)
    private String playerRef;

    public GroupMemberPO()
    {

    }

    public String getGroupRef()
    {
        return groupRef;
    }

    public void setGroupRef(String groupRef)
    {
        this.groupRef = groupRef;
    }

    public String getPlayerRef()
    {
        return playerRef;
    }

    public void setPlayerRef(String playerRef)
    {
        this.playerRef = playerRef;
    }

    @Override
    public int hashCode()
    {
        return Objects.hash(groupRef, getPlayerRef());
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
        GroupMemberPO other = (GroupMemberPO)obj;
        return Objects.equals(groupRef, other.groupRef) && Objects.equals(getPlayerRef(), other.getPlayerRef());
    }
}
