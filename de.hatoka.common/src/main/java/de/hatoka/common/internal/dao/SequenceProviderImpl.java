package de.hatoka.common.internal.dao;

import java.util.HashMap;
import java.util.Map;

import de.hatoka.common.capi.dao.SequenceProvider;
import de.hatoka.common.capi.dao.UUIDGenerator;

public class SequenceProviderImpl implements SequenceProvider
{
    Map<String, UUIDGenerator> sequences = new HashMap<>();
    private final long startPostion;

    public SequenceProviderImpl(long startPosition)
    {
        this.startPostion = startPosition;
    }

    public SequenceProviderImpl()
    {
        this(0);
    }

    @Override
    public UUIDGenerator create(String name)
    {
        return create(name, startPostion);
    }

    @Override
    public UUIDGenerator create(String name, long number)
    {
        UUIDGenerator result = sequences.get(name);
        if (result == null)
        {
            result = new SequenceImpl(number);
            sequences.put(name, result);
        }
        return result;
    }

}
