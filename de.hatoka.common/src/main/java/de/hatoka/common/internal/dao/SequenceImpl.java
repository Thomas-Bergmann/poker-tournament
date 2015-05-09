package de.hatoka.common.internal.dao;

import de.hatoka.common.capi.dao.UUIDGenerator;

public class SequenceImpl implements UUIDGenerator
{
    private long nextID;

    public SequenceImpl(long lastUsedNumber)
    {
        this.nextID = lastUsedNumber;
    }

    @Override
    public String generate()
    {
        return Long.valueOf(nextID++).toString();
    }
}
