package de.hatoka.user.capi.business;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Module;

import de.hatoka.common.capi.modules.CommonDaoModule;
import de.hatoka.mail.capi.config.SmtpConfiguration;
import de.hatoka.mail.internal.modules.MailDaoJpaModule;
import de.hatoka.mail.internal.modules.MailServiceModule;
import de.hatoka.user.capi.config.UserConfiguration;
import de.hatoka.user.internal.config.SmtpTestConfiguration;
import de.hatoka.user.internal.config.UserTestConfiguration;
import de.hatoka.user.internal.modules.UserBusinessModule;
import de.hatoka.user.internal.modules.UserDaoJpaModule;

public final class TestBusinessInjectorProvider
{
    public static Injector get(Module... modules)
    {
        List<Module> list = new ArrayList<>(Arrays.asList(modules));
        list.addAll(Arrays.asList(new CommonDaoModule(), new UserDaoJpaModule(), new UserBusinessModule(),
                        new MailDaoJpaModule(), new MailServiceModule(),
                        binder -> binder.bind(UserConfiguration.class).to(UserTestConfiguration.class).asEagerSingleton(),
                        binder -> binder.bind(SmtpConfiguration.class).to(SmtpTestConfiguration.class).asEagerSingleton()
                        ));
        return Guice.createInjector(list);
    }

    private TestBusinessInjectorProvider()
    {
    }

}
