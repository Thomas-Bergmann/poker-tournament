package de.hatoka.common.internal.dao;

import java.util.Date;
import java.util.UUID;

import org.apache.commons.lang.math.RandomUtils;

import de.hatoka.common.capi.dao.UUIDGenerator;

public class UUIDGeneratorImpl implements UUIDGenerator
{
    private int nextID = RandomUtils.nextInt();

    @Override
    public String generate()
    {
        return new UUID(nextID++ * RandomUtils.nextInt(), (new Date()).getTime()).toString();
    }
}
