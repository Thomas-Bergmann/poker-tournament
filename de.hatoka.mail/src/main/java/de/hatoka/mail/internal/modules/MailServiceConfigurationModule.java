package de.hatoka.mail.internal.modules;

import com.google.inject.Binder;
import com.google.inject.Module;

import de.hatoka.mail.capi.config.SmtpConfiguration;
import de.hatoka.mail.internal.service.SmtpConfigurationImpl;

public class MailServiceConfigurationModule implements Module
{
    @Override
    public void configure(Binder binder)
    {
        binder.bind(SmtpConfiguration.class).to(SmtpConfigurationImpl.class).asEagerSingleton();
    }

}
