package de.hatoka.tournament.capi.business;

import java.util.Objects;

import de.hatoka.user.capi.business.UserRef;

public class TournamentRef
{
    private static final String TOURNAMENT_REF_PREFIX = "tournament:";
    private final UserRef userRef;
    private final String localRef;

    public static TournamentRef valueOf(String globalRef)
    {
        if (!globalRef.startsWith(TOURNAMENT_REF_PREFIX))
        {
            throw new IllegalArgumentException("ref '"+globalRef+"' is not a tournament");
        }
        int findAt = globalRef.indexOf("@");
        UserRef userRef = UserRef.valueOfGlobal(globalRef.substring(findAt + 1));
        return new TournamentRef(userRef, globalRef.substring(TOURNAMENT_REF_PREFIX.length(), findAt));
    }

    public static TournamentRef valueOf(UserRef userRef, String localRef)
    {
        return new TournamentRef(userRef, localRef);
    }

    private TournamentRef(UserRef userRef, String localRef)
    {
        this.userRef = userRef;
        this.localRef = localRef;
    }

    @Override
    public String toString()
    {
        return getGlobalRef();
    }

    public String getGlobalRef()
    {
        return TOURNAMENT_REF_PREFIX + localRef + "@" + userRef;
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
        TournamentRef other = (TournamentRef)obj;
        return Objects.equals(localRef, other.localRef) && Objects.equals(userRef, other.userRef);
    }
}
