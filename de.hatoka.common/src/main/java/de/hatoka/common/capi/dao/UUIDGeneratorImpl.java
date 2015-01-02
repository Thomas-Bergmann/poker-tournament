package de.hatoka.common.capi.dao;

import java.util.Date;
import java.util.UUID;

import org.apache.commons.lang.math.RandomUtils;

public class UUIDGeneratorImpl implements UUIDGenerator
{
    private int nextID = RandomUtils.nextInt();

    @Override
    public String generate()
    {
        return new UUID(nextID++ * RandomUtils.nextInt(), (new Date()).getTime()).toString();
    }
}
