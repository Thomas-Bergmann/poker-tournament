package de.hatoka.group.modules;

import com.google.inject.Binder;
import com.google.inject.Module;

import de.hatoka.group.capi.dao.GroupDao;
import de.hatoka.group.capi.dao.MemberDao;
import de.hatoka.group.internal.dao.GroupDaoJpa;
import de.hatoka.group.internal.dao.MemberDaoJpa;

public class GroupDaoJpaModule implements Module
{
    @Override
    public void configure(Binder binder)
    {
        binder.bind(GroupDao.class).to(GroupDaoJpa.class).asEagerSingleton();
        binder.bind(MemberDao.class).to(MemberDaoJpa.class).asEagerSingleton();
    }
}
