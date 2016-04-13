package de.hatoka.account.capi.business;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Module;

import de.hatoka.account.internal.modules.AccountBusinessModule;
import de.hatoka.account.internal.modules.AccountConfigurationModule;
import de.hatoka.account.internal.modules.AccountDaoJpaModule;
import de.hatoka.common.capi.modules.CommonDaoModule;
import de.hatoka.mail.internal.modules.MailDaoJpaModule;
import de.hatoka.mail.internal.modules.MailServiceConfigurationModule;
import de.hatoka.mail.internal.modules.MailServiceModule;

public final class TestBusinessInjectorProvider
{
    public static Injector get(Module... modules)
    {
        List<Module> list = new ArrayList<>(Arrays.asList(modules));
        list.addAll(Arrays.asList(new CommonDaoModule(), new AccountDaoJpaModule(),
                        new AccountBusinessModule(), new MailDaoJpaModule(),
                        new MailServiceModule(), new MailServiceConfigurationModule(), new AccountConfigurationModule()));
        return Guice.createInjector(list);
    }

    private TestBusinessInjectorProvider()
    {
    }

}
