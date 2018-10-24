package de.hatoka.player.capi.business;

import java.util.Objects;

import de.hatoka.user.capi.business.UserRef;

public class PlayerRef
{
    private static final String PLAYER_REF_PREFIX = "player:";
    private final UserRef userRef;
    private final String localRef;

    public static PlayerRef valueOf(String globalRef)
    {
        if (!globalRef.startsWith(PLAYER_REF_PREFIX))
        {
            throw new IllegalArgumentException("ref '"+globalRef+"' is not a tournament");
        }
        int findAt = globalRef.indexOf("@");
        UserRef userRef = UserRef.valueOfGlobal(globalRef.substring(findAt + 1));
        return new PlayerRef(userRef, globalRef.substring(PLAYER_REF_PREFIX.length(), findAt));
    }

    public static PlayerRef valueOf(UserRef userRef, String localRef)
    {
        return new PlayerRef(userRef, localRef);
    }

    private PlayerRef(UserRef userRef, String localRef)
    {
        this.userRef = userRef;
        this.localRef = localRef;
    }

    @Override
    public String toString()
    {
        return getGlobalRef();
    }

    public String getLocalRef()
    {
        return localRef;
    }

    public String getGlobalRef()
    {
        return "player:" + localRef + "@" + userRef;
    }

    public UserRef getUserRef()
    {
        return userRef;
    }

    @Override
    public int hashCode()
    {
        return Objects.hash(localRef, userRef);
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
        PlayerRef other = (PlayerRef)obj;
        return Objects.equals(localRef, other.localRef) && Objects.equals(userRef, other.userRef);
    }
}
