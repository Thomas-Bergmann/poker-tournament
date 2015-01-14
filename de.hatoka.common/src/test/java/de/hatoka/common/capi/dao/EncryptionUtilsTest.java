package de.hatoka.common.capi.dao;

import javax.inject.Inject;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.google.inject.Guice;
import com.google.inject.Injector;

import de.hatoka.common.capi.modules.CommonDaoModule;

public class EncryptionUtilsTest
{
    private static final String SIMPLE_PASSWORD = "123456789abcdefghi";

    @Inject
    private EncryptionUtils underTest;

    @Before
    public void inject()
    {
        Injector injector = Guice.createInjector(new CommonDaoModule());
        injector.injectMembers(this);
    }

    @Test
    public void testSimplePassword()
    {
        String enc = underTest.sign(SIMPLE_PASSWORD);
        Assert.assertNotNull("encrypted string not available", enc);
        String encAgain = underTest.sign(SIMPLE_PASSWORD);
        Assert.assertEquals("encrypted string not equal at second time", enc, encAgain);
        Assert.assertFalse("encrypted string is not password", enc.equals(SIMPLE_PASSWORD));
    }
}
