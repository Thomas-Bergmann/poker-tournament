package de.hatoka.account.internal.app;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import javax.ws.rs.ApplicationPath;
import javax.ws.rs.core.Application;

import com.google.inject.Guice;
import com.google.inject.Injector;

import de.hatoka.account.internal.app.servlets.AccountService;
import de.hatoka.account.internal.app.servlets.LoginService;
import de.hatoka.account.internal.modules.AccountBusinessModule;
import de.hatoka.account.internal.modules.AccountConfigurationModule;
import de.hatoka.account.internal.modules.AccountDaoJpaModule;
import de.hatoka.address.internal.modules.AddressBusinessModule;
import de.hatoka.address.internal.modules.AddressDaoModule;
import de.hatoka.common.capi.app.servlet.ResourceService;
import de.hatoka.common.capi.app.servlet.ServletConstants;
import de.hatoka.common.capi.modules.CommonDaoModule;
import de.hatoka.common.capi.modules.JpaDBModule;
import de.hatoka.mail.internal.modules.MailDaoJpaModule;
import de.hatoka.mail.internal.modules.MailServiceConfigurationModule;
import de.hatoka.mail.internal.modules.MailServiceModule;

/**
 * Registers all request resources
 */
@ApplicationPath("/account")
public class AccountApplication extends Application
{
    private final Injector injector;

    public AccountApplication()
    {
        injector = Guice.createInjector(new CommonDaoModule(), new AddressDaoModule(), new AddressBusinessModule(),
                        new AccountDaoJpaModule(), new AccountBusinessModule(), new JpaDBModule("AccountPU"),
                        new MailDaoJpaModule(), new MailServiceModule(), new MailServiceConfigurationModule(), new AccountConfigurationModule());
    }

    @Override
    public Set<Class<?>> getClasses()
    {
        final Set<Class<?>> result = new HashSet<>();
        result.add(ResourceService.class);
        result.add(LoginService.class);
        result.add(AccountService.class);
        return result;
    }

    @Override
    public Map<String, Object> getProperties()
    {
        Map<String, Object> result = new HashMap<>();
        result.put(ServletConstants.PROPERTY_INJECTOR, injector);
        return result;
    }
}