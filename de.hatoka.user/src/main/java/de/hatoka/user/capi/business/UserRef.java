package de.hatoka.user.capi.business;

import java.util.Objects;

public class UserRef
{
    private static final String USER_REF_PREFIX = "user:";
    private final String localRef;

    /**
     * @param localRef local user reference (like "abc")
     * @return user ref
     */
    public static UserRef valueOfLocal(String localRef)
    {
        return new UserRef(localRef);
    }

    /**
     * @param globalRef global user reference (like "user:abc")
     * @return
     */
    public static UserRef valueOfGlobal(String globalRef)
    {
        if (globalRef.startsWith(USER_REF_PREFIX))
        {
            return new UserRef(globalRef.substring(USER_REF_PREFIX.length()));
        }
        throw new IllegalArgumentException("ref '"+globalRef+"' is not a user");
    }

    private UserRef(String localRef)
    {
        this.localRef = localRef;
    }

    public String getLocalRef()
    {
        return localRef;
    }

    public String getGlobalRef()
    {
        return USER_REF_PREFIX + localRef;
    }

    @Override
    public String toString()
    {
        return getGlobalRef();
    }

    @Override
    public int hashCode()
    {
        return Objects.hash(localRef);
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
        UserRef other = (UserRef)obj;
        return Objects.equals(localRef, other.localRef);
    }
}
