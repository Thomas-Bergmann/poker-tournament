package de.hatoka.common.capi.modules;

import static org.junit.Assert.assertNotNull;

import org.junit.Test;

import com.google.inject.Guice;
import com.google.inject.Injector;

import de.hatoka.common.capi.dao.UUIDGenerator;

public class CommonDaoModuleTest
{
    @Test
    public void testUUIDGenerator()
    {
        Injector injector = Guice.createInjector(new CommonDaoModule());
        UUIDGenerator uuidGenerator = injector.getInstance(UUIDGenerator.class);
        assertNotNull("Generator not instantiated", uuidGenerator);
    }
}
