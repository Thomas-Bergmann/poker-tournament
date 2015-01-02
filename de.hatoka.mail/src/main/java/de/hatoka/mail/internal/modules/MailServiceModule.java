package de.hatoka.mail.internal.modules;

import com.google.inject.Binder;
import com.google.inject.Module;

import de.hatoka.mail.capi.service.MailService;
import de.hatoka.mail.internal.service.SmtpMailService;

public class MailServiceModule implements Module
{
    @Override
    public void configure(Binder binder)
    {
        binder.bind(MailService.class).to(SmtpMailService.class).asEagerSingleton();
    }

}
