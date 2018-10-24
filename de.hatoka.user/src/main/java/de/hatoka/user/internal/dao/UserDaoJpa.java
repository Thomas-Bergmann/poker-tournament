package de.hatoka.user.internal.dao;

import java.util.List;

import javax.inject.Inject;

import de.hatoka.common.capi.dao.GenericJPADao;
import de.hatoka.common.capi.dao.UUIDGenerator;
import de.hatoka.user.capi.dao.UserDao;
import de.hatoka.user.capi.entities.UserPO;

public class UserDaoJpa extends GenericJPADao<UserPO> implements UserDao
{
    @Inject
    private UUIDGenerator uuidGenerator;

    public UserDaoJpa()
    {
        super(UserPO.class);
    }

    @Override
    public UserPO createAndInsert(String externalRef)
    {
        UserPO result = create();
        result.setId(uuidGenerator.generate());
        result.setExternalRef(externalRef);
        insert(result);
        return result;
    }

    @Override
    public UserPO getByExternalRef(String externalRef)
    {
        List<UserPO> result = createNamedQuery("UserPO.findByExternalRef").setParameter("externalRef", externalRef).getResultList();
        if (result.isEmpty())
        {
            return null;
        }
        if (result.size() > 1)
        {
            throw new IllegalStateException("Duplicate alternate key found. ExternalRef: '" + externalRef + "'");
        }
        return result.get(0);
    }

}
