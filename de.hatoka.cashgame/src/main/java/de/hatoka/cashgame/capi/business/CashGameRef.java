package de.hatoka.cashgame.capi.business;

import java.util.Objects;

import de.hatoka.user.capi.business.UserRef;

public class CashGameRef
{
    private static final String CASHGAME_REF_PREFIX = "cashgame:";
    private final UserRef userRef;
    private final String localRef;

    public static CashGameRef valueOf(String globalRef)
    {
        if (!globalRef.startsWith(CASHGAME_REF_PREFIX))
        {
            throw new IllegalArgumentException("ref '"+globalRef+"' is not a cashgame");
        }
        int findAt = globalRef.indexOf("@");
        UserRef userRef = UserRef.valueOfGlobal(globalRef.substring(findAt + 1));
        return new CashGameRef(userRef, globalRef.substring(CASHGAME_REF_PREFIX.length(), findAt));
    }

    public static CashGameRef valueOf(UserRef userRef, String localRef)
    {
        return new CashGameRef(userRef, localRef);
    }

    private CashGameRef(UserRef userRef, String localRef)
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
        return CASHGAME_REF_PREFIX + localRef + "@" + userRef;
    }

    public UserRef getUserRef()
    {
        return userRef;
    }


    public String getLocalRef()
    {
        return localRef;
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
        CashGameRef other = (CashGameRef)obj;
        return Objects.equals(localRef, other.localRef) && Objects.equals(userRef, other.userRef);
    }
}
