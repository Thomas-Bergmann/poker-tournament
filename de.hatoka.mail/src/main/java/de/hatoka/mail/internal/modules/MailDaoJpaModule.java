package de.hatoka.mail.internal.modules;

import com.google.inject.Binder;
import com.google.inject.Module;

import de.hatoka.mail.capi.dao.MailDao;
import de.hatoka.mail.capi.dao.MessageIDGenerator;
import de.hatoka.mail.internal.dao.MailDaoJpa;
import de.hatoka.mail.internal.dao.MessageIDGeneratorImpl;

public class MailDaoJpaModule implements Module
{
    @Override
    public void configure(Binder binder)
    {
        binder.bind(MessageIDGenerator.class).to(MessageIDGeneratorImpl.class).asEagerSingleton();
        binder.bind(MailDao.class).to(MailDaoJpa.class).asEagerSingleton();
    }

}
