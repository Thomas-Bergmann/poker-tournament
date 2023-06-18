package de.hatoka.group.capi.business;

import java.util.Objects;

public class GroupRef
{
    private static final String GROUP_REF_PREFIX = "group:";
    private final String localRef;

    /**
     * @param localRef local user reference (like "abc")
     * @return user ref
     */
    public static GroupRef valueOfLocal(String localRef)
    {
        return new GroupRef(localRef);
    }

    /**
     * @param globalRef global user reference (like "group:abc")
     * @return
     */
    public static GroupRef valueOfGlobal(String globalRef)
    {
        if (globalRef.startsWith(GROUP_REF_PREFIX))
        {
            return new GroupRef(globalRef.substring(GROUP_REF_PREFIX.length()));
        }
        throw new IllegalArgumentException("ref '"+globalRef+"' is not a group");
    }

    private GroupRef(String localRef)
    {
        this.localRef = localRef;
    }

    public String getLocalRef()
    {
        return localRef;
    }

    public String getGlobalRef()
    {
        return GROUP_REF_PREFIX + localRef;
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
        GroupRef other = (GroupRef)obj;
        return Objects.equals(localRef, other.localRef);
    }
}
