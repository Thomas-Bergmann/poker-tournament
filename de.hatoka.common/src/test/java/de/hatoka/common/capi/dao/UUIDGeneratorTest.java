package de.hatoka.common.capi.dao;

import static org.junit.Assert.assertEquals;

import java.util.HashSet;
import java.util.Set;

import javax.inject.Inject;

import org.junit.Before;
import org.junit.Test;

import com.google.inject.Guice;
import com.google.inject.Injector;

import de.hatoka.common.capi.modules.CommonDaoModule;

public class UUIDGeneratorTest
{
    private static final int NUMBER_OF_KEYS = 1000;

    @Inject
    private UUIDGenerator underTest;

    @Before
    public void inject()
    {
        Injector injector = Guice.createInjector(new CommonDaoModule());
        injector.injectMembers(this);
    }
    @Test
    public void testDifference()
    {
        Set<String> keys = new HashSet<>();
        for (int i = 0; i < NUMBER_OF_KEYS; i++)
        {
            keys.add(underTest.generate());
        }
        assertEquals("Same key produced", NUMBER_OF_KEYS, keys.size());
    }
}
